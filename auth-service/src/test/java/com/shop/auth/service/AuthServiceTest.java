package com.shop.auth.service;

import com.shop.common.service.auth.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void deliverTokenByRPCTest () {
        DeliveryResp deliveryResp = authService.deliverTokenByRPC(DeliverTokenReq.builder().userId(1L).build());
        assert deliveryResp.getToken() != null;
        VerifyResp verifyResp = authService.verifyTokenByRPC(VerifyTokenReq.builder().token(deliveryResp.getToken()).build());
        assert verifyResp.isRes();
    }

    @Test
    public void verifyTokenByRPCTest () {
        VerifyResp verifyResp = authService.verifyTokenByRPC(VerifyTokenReq.builder().token("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoicmlPbFFtRnFBVlNyOG9Kd3cxWHVQUkJ0S1hGYzBlVjQifQ.KbNXpvjWp46N3kG6MlnW9OQmKcxUj9MwUHj0SsTQl5U").build());
        assert verifyResp.isRes();
    }

}
