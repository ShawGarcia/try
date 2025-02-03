package com.shop.common.service.auth;


public interface AuthService {
    /**
     * 根据用户 ID 生成令牌（Token）
     * @param request
     * @return
     */
    DeliveryResp deliverTokenByRPC(DeliverTokenReq request);

    /**
     * 验证令牌是否有效
     * @param request
     * @return
     */
    VerifyResp verifyTokenByRPC(VerifyTokenReq request);
}