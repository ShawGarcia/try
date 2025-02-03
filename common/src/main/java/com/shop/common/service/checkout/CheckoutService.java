package com.shop.common.service.checkout;

import com.shop.common.service.payment.ChargeResp;

public interface CheckoutService {
    /**
     * 处理结账请求
     * @param request
     * @return
     */
    CheckoutResp checkout(CheckoutReq request);

    String testGray();
}