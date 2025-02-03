package com.shop.common.service.payment;

import com.shop.common.exception.PaymentException;

public interface PaymentService {
    ChargeResp charge(ChargeReq request) throws PaymentException;
}