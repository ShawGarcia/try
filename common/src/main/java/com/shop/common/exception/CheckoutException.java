package com.shop.common.exception;

public class CheckoutException extends RuntimeException {

    public CheckoutException() {
        super();
    }

    public CheckoutException(String message) {
        super(message);
    }

    public CheckoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckoutException(Throwable cause) {
        super(cause);
    }

}
