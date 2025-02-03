package com.shop.gateway.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.shop.common.entity.ApiResponse;
import com.shop.common.service.payment.ChargeReq;
import com.shop.common.service.payment.ChargeResp;
import com.shop.common.service.payment.PaymentService;
import com.shop.gateway.config.SentinelBlockHandler;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {
    @DubboReference
    private PaymentService paymentService;

    @PostMapping("/api/payment/charge")
    @SentinelResource(
            value = "payment.charge",
            blockHandlerClass = SentinelBlockHandler.class,
            blockHandler = "blockHandler",
            fallbackClass = SentinelBlockHandler.class,
            fallback = "fallback")
    public ApiResponse<ChargeResp> charge(@RequestBody @Valid ChargeReq chargeReq) {
        return ApiResponse.success(paymentService.charge(chargeReq));
    }
}