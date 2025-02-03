package com.shop.common.exception;

import java.io.Serial;
import java.io.Serializable;

/**
 * 支付相关的自定义异常
 */
public class PaymentException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
} 