package com.shop.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.shop.common.service.auth.AuthService;
import com.shop.common.service.auth.VerifyTokenReq;
import com.shop.gateway.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String ERROR_MESSAGE = "Unauthorized";

    @DubboReference
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!validateAndProcessToken(request, response)) {
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateAndProcessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = extractToken(request);
        if (token == null) {
            handleUnauthorized(response);
            return false;
        }

        return verifyToken(token, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        if (StrUtil.isEmpty(token)) {
            log.debug("Missing Authorization header");
            return null;
        }
        return token;
    }

    private boolean verifyToken(String token, HttpServletResponse response) throws IOException {
        try {
            return authService.verifyTokenByRPC(new VerifyTokenReq(token)).res;
        } catch (Exception e) {
            log.error("Token verification failed", e);
            handleUnauthorized(response);
            return false;
        }
    }

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        ResponseUtils.writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ERROR_MESSAGE);
    }

    // 可以添加白名单路径检查
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return isWhitelistedPath(path);
    }

    private boolean isWhitelistedPath(String path) {
        // 添加不需要验证的路径
        return path.startsWith("/public/") ||
                path.equals("/login") ||
                path.equals("/register");
    }
}