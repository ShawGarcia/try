package com.shop.gateway.filter;

import com.shop.common.utils.IpUtils;
import com.shop.gateway.config.TrafficColorConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class TrafficTagFilter extends OncePerRequestFilter implements Ordered {

    private static final String COLOR_HEADER = CommonConstants.TAG_KEY;
    private static final String UNKNOWN_COLOR = "default";

    @Autowired
    private TrafficColorConfig colorConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String color = determineTrafficColor(request);
        if (! Objects.equals(color, UNKNOWN_COLOR)) {
            request.setAttribute(COLOR_HEADER, color);
        }

        // 继续执行后续过滤器链
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return -1; // 设置过滤器的优先级
    }

    private String determineTrafficColor(HttpServletRequest request) {
        String clientIp = IpUtils.getClientIp(request);
        return colorConfig.matchColor(clientIp).orElse(UNKNOWN_COLOR);
    }
}