package com.shop.gateway.config;

import com.shop.common.entity.ApiResponse;
import com.shop.common.exception.BizException;
import com.shop.common.exception.PaymentException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ApiResponse<?> handleValidationException(Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else if (ex instanceof BindException) {
            message = ((BindException) ex).getBindingResult().getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            message = ((ConstraintViolationException) ex).getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
        }
        
        log.warn("参数校验失败: {}", message);
        return ApiResponse.error(400, message);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> handleException(Throwable ex) {
        log.error("系统异常: ", ex);
        return ApiResponse.error(500, "系统异常，请稍后重试");
    }

    @ExceptionHandler(PaymentException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> handlePaymentException(PaymentException ex) {
        log.warn("支付处理异常: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> handleBizException(BizException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(RpcException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> handleRpcException(RpcException ex) {
        log.error("RPC 异常原因：{} - {}", ex.getCode(), ex.getClass().getSimpleName());
        return switch (ex.getCode()) {
            case RpcException.TIMEOUT_EXCEPTION -> ApiResponse.error(504, "下游服务响应超时");
            case RpcException.NETWORK_EXCEPTION -> ApiResponse.error(503, "服务通信故障");
            case RpcException.FORBIDDEN_EXCEPTION -> ApiResponse.error(503, "下游服务暂时不可用");
            default -> ApiResponse.error(502, "服务调用失败");
        };
    }
}