package com.shop.common.service.order;

public interface OrderService {
    PlaceOrderResp placeOrder(PlaceOrderReq request);

    ListOrderResp listOrder(ListOrderReq request);

    MarkOrderPaidResp markOrderPaid(MarkOrderPaidReq request);
}