package com.shop.checkout.controller;

import com.shop.common.entity.ApiResponse;
import com.shop.common.service.checkout.CheckoutReq;
import com.shop.common.service.checkout.CheckoutResp;
import com.shop.common.service.checkout.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    /**
     * 结算
     * @param request
     * @return
     */
    @PostMapping("/checkout")
    public ApiResponse<CheckoutResp> checkout(@RequestBody CheckoutReq request) {
        return ApiResponse.success(checkoutService.checkout(request));
    }

    @GetMapping("/testElk")
    public String testElk() {
        log.info("testElk");
        return "testElk";
    }

}
