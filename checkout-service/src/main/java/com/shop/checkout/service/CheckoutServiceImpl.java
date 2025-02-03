package com.shop.checkout.service;

import org.apache.dubbo.common.constants.CommonConstants;
import com.shop.common.exception.CheckoutException;
import com.shop.common.service.cart.*;
import com.shop.common.service.checkout.CheckoutReq;
import com.shop.common.service.checkout.CheckoutResp;
import com.shop.common.service.checkout.CheckoutService;
import com.shop.common.service.order.*;
import com.shop.common.service.payment.ChargeReq;
import com.shop.common.service.payment.ChargeResp;
import com.shop.common.service.payment.PaymentService;
import com.shop.common.service.product.GetProductReq;
import com.shop.common.service.product.GetProductResp;
import com.shop.common.service.product.ProductCatalogService;
import jakarta.validation.Valid;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DubboService
@Service
public class CheckoutServiceImpl implements CheckoutService {

    @DubboReference
    private CartService cartService;

    @DubboReference
    private OrderService orderService;

    @DubboReference
    private PaymentService paymentService;

    @DubboReference
    private ProductCatalogService productCatalogService;

    /**
     * 结算流程：
     * 获取购物车：从购物车服务获取用户的购物车信息。
     * 计算购物车总价：从商品服务获取商品详情，并计算购物车总价。
     * 创建订单：调用订单服务创建订单。
     * 清空购物车：调用购物车服务清空用户的购物车。
     * 支付：调用支付服务完成支付。
     * 发送邮件通知：通过消息队列发送邮件通知。
     * 标记订单为已支付：调用订单服务将订单状态更新为“已支付”。
     * 返回结果：返回订单 ID 和交易 ID。
     * @param request
     * @return
     */
    @Override
    public CheckoutResp checkout(@Valid CheckoutReq request) {
        // get cart
        GetCartReq getCartReq = new GetCartReq();
        getCartReq.setUserId(request.getUserId());
        GetCartResp getCartResp = cartService.getCart(getCartReq);
        Optional.ofNullable(getCartResp)
                .map(GetCartResp::getCart)
                .map(Cart::getItems)
                .filter(items -> !items.isEmpty())
                .orElseThrow(() -> new CheckoutException("购物车为空，无法结算"));
        float totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : getCartResp.getCart().getItems()) {
            GetProductReq getProductReq = new GetProductReq();
            getProductReq.setId(cartItem.getProductId());
            GetProductResp product = productCatalogService.getProduct(getProductReq);
            if (product == null || product.getProduct() == null) {
                continue;
            }
            float cost = (product.getProduct().getPrice()) * ((float)cartItem.getQuantity());
            totalPrice += cost;
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProduct().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setCost(cost);
            orderItems.add(orderItem);
        }
        // create order
        PlaceOrderReq placeOrderReq = new PlaceOrderReq();
        placeOrderReq.setUserId(request.getUserId());
        placeOrderReq.setOrderItems(orderItems);
        placeOrderReq.setAddress(request.getAddress());
        placeOrderReq.setEmail(request.getEmail());
        placeOrderReq.setUserCurrency("USD");
        PlaceOrderResp placeOrderResp = orderService.placeOrder(placeOrderReq);
        Optional.ofNullable(placeOrderResp)
                .map(PlaceOrderResp::getOrderId)
                .orElseThrow(() -> new CheckoutException("订单创建失败"));
        // empty cart
        EmptyCartReq emptyCartReq = new EmptyCartReq();
        emptyCartReq.setUserId(request.getUserId());
        EmptyCartResp emptyCartResp = cartService.emptyCart(emptyCartReq);
        Optional.ofNullable(emptyCartResp)
                .orElseThrow(() -> new CheckoutException("置空购物车失败"));
        // charge
        ChargeReq chargeReq = new ChargeReq();
        chargeReq.setUserId(request.getUserId());
        chargeReq.setAmount(totalPrice);
        chargeReq.setOrderId(placeOrderResp.getOrderId());
        chargeReq.setCreditCard(request.getCreditCard());
        ChargeResp chargeResp = paymentService.charge(chargeReq);
        Optional.ofNullable(chargeResp)
                .map(ChargeResp::getTransactionId)
                .orElseThrow(() -> new CheckoutException("支付操作失败"));
        // todo send email

        // change order state
        MarkOrderPaidReq markOrderPaidReq = new MarkOrderPaidReq();
        markOrderPaidReq.setUserId(request.getUserId());
        markOrderPaidReq.setOrderId(placeOrderResp.getOrderId());
        MarkOrderPaidResp markOrderPaidResp = orderService.markOrderPaid(markOrderPaidReq);
        Optional.ofNullable(markOrderPaidResp)
                .orElseThrow(() -> new CheckoutException("修改订单状态失败"));

        return CheckoutResp.builder()
                    .orderId(placeOrderResp.getOrderId())
                    .transactionId(chargeResp.getTransactionId())
                    .build();
    }

    @Override
    public String testGray() {
        return RpcContext.getCurrentServiceContext().getAttachment(CommonConstants.TAG_KEY);
    }
}
