package com.shop.common.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {
    private final Integer code;

    public BizException(String message) {
        super(message);
        this.code = 500;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public static void fail(String message) {
        throw new BizException(message);
    }

    public static void fail(Integer code, String message) {
        throw new BizException(code, message);
    }
}