package com.shop.gateway.controller;

import com.shop.common.entity.ApiResponse;
import com.shop.common.service.checkout.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CheckoutController {

    @DubboReference
    private CheckoutService checkoutService;

    @GetMapping("/public/checkout/testGray")
    public ApiResponse<String> testGray() {
        return ApiResponse.success(checkoutService.testGray());
    }

}
