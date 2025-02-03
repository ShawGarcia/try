package com.shop.common.service.user;

public interface UserService {
    RegisterResp register(RegisterReq request);

    LoginResp login(LoginReq request);
}