package com.shop.common.service.checkout;

public interface CheckoutService {
    /**
     * 处理结账请求
     * @param request
     * @return
     */
    CheckoutResp checkout(CheckoutReq request);
}