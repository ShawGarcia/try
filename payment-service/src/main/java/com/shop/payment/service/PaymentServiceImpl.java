package com.shop.payment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.common.exception.PaymentException;
import com.shop.common.service.payment.ChargeReq;
import com.shop.common.service.payment.ChargeResp;
import com.shop.common.service.payment.PaymentService;
import com.shop.common.utils.RedisLockUtil;
import com.shop.payment.entity.po.PaymentPo;
import com.shop.payment.mapper.PaymentMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@DubboService
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_LOCK_KEY_PREFIX = "payment:lock:order:";
    private static final int LOCK_TIMEOUT_SECONDS = 30;
    private static final int LOCK_WAIT_SECONDS = 5;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private RedisLockUtil redisLockUtil;

    @Resource
    private CreditCardValidationService creditCardValidationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChargeResp charge(@Valid ChargeReq request) {
        String lockKey = PAYMENT_LOCK_KEY_PREFIX + request.getOrderId();

        try {
            boolean locked = redisLockUtil.tryLock(lockKey, LOCK_TIMEOUT_SECONDS, LOCK_WAIT_SECONDS);
            if (!locked) {
                throw new PaymentException("支付处理中,请稍后重试");
            }

            checkDuplicatePayment(request.getOrderId());

            creditCardValidationService.validate(request.getCreditCard());

            String transactionId = generateTransactionId();

            savePaymentLog(request, transactionId);

            log.info("订单支付成功 - 订单号: {}, 交易号: {}", request.getOrderId(), transactionId);

            return ChargeResp.builder()
                    .transactionId(transactionId)
                    .build();

        } catch (PaymentException e) {
            log.error("支付失败 - 订单号: {}, 原因: {}", request.getOrderId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("支付异常 - 订单号: {}", request.getOrderId(), e);
            throw new PaymentException("支付处理失败");
        } finally {
            redisLockUtil.unlock(lockKey);
        }
    }

    private void checkDuplicatePayment(String orderId) {
        LambdaQueryWrapper<PaymentPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentPo::getOrderId, orderId);
        if (paymentMapper.selectCount(queryWrapper) > 0) {
            throw new PaymentException("订单已支付");
        }
    }

    private String generateTransactionId() {
        String transactionId = UUID.randomUUID().toString().replace("-", "");
        LambdaQueryWrapper<PaymentPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentPo::getTransactionId, transactionId);

        if (paymentMapper.selectCount(queryWrapper) > 0) {
            return generateTransactionId();
        }
        return transactionId;
    }

    private void savePaymentLog(ChargeReq request, String transactionId) {
        PaymentPo paymentPo = PaymentPo.builder()
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .transactionId(transactionId)
                .amount(BigDecimal.valueOf(request.getAmount()))
                .payAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        paymentMapper.insert(paymentPo);
    }
}