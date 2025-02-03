package com.shop.auth.service;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.shop.common.service.auth.*;
import jakarta.validation.Valid;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * 根据用户 ID 生成令牌（Token）
     * @param request
     * @return
     */
    @Override
    public DeliveryResp deliverTokenByRPC(@Valid DeliverTokenReq request) {
        StpUtil.login(request.getUserId());
        String token = StpUtil.getTokenValueByLoginId(request.getUserId());
        return DeliveryResp.builder()
                .token(token)
                .build();
    }

    /**
     * 验证令牌是否有效+续期
     * @param request
     * @return
     */
    @Override
    public VerifyResp verifyTokenByRPC(@Valid VerifyTokenReq request) {
        boolean isValid = true;
        try {
            // 检查令牌是否有效
            StpUtil.getLoginIdByToken(request.getToken());
            // 为指定Token续签
            StpUtil.stpLogic.updateLastActiveToNow(request.getToken());
        } catch (Exception e) {
            isValid = false;
        }
        return VerifyResp.builder()
                .res(isValid)
                .build();
    }
}