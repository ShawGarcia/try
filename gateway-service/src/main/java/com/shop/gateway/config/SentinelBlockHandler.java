package com.shop.gateway.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.shop.common.entity.ApiResponse;
import com.shop.common.service.payment.ChargeReq;
import com.shop.common.service.payment.ChargeResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentinelBlockHandler {
    
    public static ApiResponse<ChargeResp> blockHandler(ChargeReq chargeReq, BlockException ex) {
        log.warn("Request blocked by Sentinel: {}", chargeReq, ex);
        return ApiResponse.error("请求触发限流规则");
    }

    public static ApiResponse<ChargeResp> fallback(ChargeReq chargeReq, Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        log.error("服务降级触发，原因：{}", throwable.getClass().getSimpleName());
        return ApiResponse.error("服务暂时不可用，建议稍后重试（错误代码：503）");
    }
} 