package com.shop.user.service;

import com.shop.common.service.auth.AuthService;
import com.shop.common.service.user.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class UserServiceImpl implements UserService {
     @DubboReference
     private AuthService authService;

    @Override
    public RegisterResp register(RegisterReq request) {
        return null;
    }

    @Override
    public LoginResp login(LoginReq request) {
        authService.deliverTokenByRPC(null);
        return new LoginResp(1);
    }
}