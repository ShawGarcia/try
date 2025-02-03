//package com.shop.gateway.filter;
//
//import cn.hutool.core.util.StrUtil;
//import com.shop.gateway.utils.ResponseUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.commons.codec.digest.HmacAlgorithms;
//import org.apache.commons.codec.digest.HmacUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.*;
//
//@Component
//public class SecurityFilter extends OncePerRequestFilter {
//
//    private static final String SECRET_KEY = "47883490348210471";
//    private static final long TIMESTAMP_THRESHOLD = 5 * 60 * 1000; // 5分钟
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            // 获取请求头
//            String signature = request.getHeader("Signature");
//            String timestamp = request.getHeader("Timestamp");
//            String nonce = request.getHeader("Nonce");
//
//            // 校验时间戳
//            if (!isValidTimestamp(timestamp)) {
//                ResponseUtils.writeError(response, 400, "Invalid or missing timestamp");
//                return;
//            }
//
//            // 防重放攻击
//            if (isReplayAttack(nonce)) {
//                ResponseUtils.writeError(response, 400, "Replay attack detected");
//                return;
//            }
//
//            // 校验签名
//            if (!isValidSignature(request, signature)) {
//                ResponseUtils.writeError(response, 400, "Invalid signature");
//                return;
//            }
//
//            // 记录 nonce，防止重放攻击
//            redisTemplate.opsForValue().set(nonce, "used");
//
//            // 继续处理请求
//            filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            ResponseUtils.writeError(response, 500, "Internal server error: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 校验时间戳是否有效
//     */
//    private boolean isValidTimestamp(String timestamp) {
//        if (timestamp == null || timestamp.trim().isEmpty()) {
//            return false;
//        }
//
//        try {
//            long clientTimestamp = Long.parseLong(timestamp);
//            long serverTimestamp = System.currentTimeMillis();
//            return Math.abs(serverTimestamp - clientTimestamp) <= TIMESTAMP_THRESHOLD;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    /**
//     * 检查是否为重放攻击
//     */
//    private boolean isReplayAttack(String nonce) {
//        if (StrUtil.isEmpty(nonce)) {
//            return true;
//        }
//        return redisTemplate.hasKey(nonce);
//    }
//
//    /**
//     * 校验签名是否有效
//     */
//    private boolean isValidSignature(HttpServletRequest request, String signature) {
//        if (signature == null || signature.trim().isEmpty()) {
//            return false;
//        }
//
//        // 获取所有请求参数
//        Map<String, String> params = new HashMap<>();
//        Enumeration<String> paramNames = request.getParameterNames();
//        while (paramNames.hasMoreElements()) {
//            String paramName = paramNames.nextElement();
//            params.put(paramName, request.getParameter(paramName));
//        }
//
//        // 按字典序排序
//        List<String> keys = new ArrayList<>(params.keySet());
//        Collections.sort(keys);
//
//        // 拼接参数
//        StringBuilder sb = new StringBuilder();
//        for (String key : keys) {
//            sb.append(key).append("=").append(params.get(key)).append("&");
//        }
//        String paramString = sb.toString();
//        if (!StrUtil.isEmpty(paramString)) {
//            paramString = paramString.substring(0, paramString.length() - 1);
//        }
//
//        // 生成签名
//        String serverSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, SECRET_KEY).hmacHex(paramString);
//
//        return serverSignature.equals(signature);
//    }
//}