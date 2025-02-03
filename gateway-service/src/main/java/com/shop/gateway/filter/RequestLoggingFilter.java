package com.shop.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 包装响应
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // 包装请求以缓存请求体
        CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(cachedBodyRequest, responseWrapper);
        } finally {
            // 记录请求信息
            String requestBody = new String(cachedBodyRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
            String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";

            // 获取请求头
            String headers = Collections.list(request.getHeaderNames())
                    .stream()
                    .map(headerName -> headerName + ": " + request.getHeader(headerName))
                    .collect(Collectors.joining(", "));

            // 计算处理时间
            long duration = System.currentTimeMillis() - startTime;

            // 构建日志信息
            String logMessage = "\n================== Request Log Begin ==================\n" +
                    "URI         : " + request.getRequestURI() + queryString + "\n" +
                    "Method      : " + request.getMethod() + "\n" +
                    "Headers     : " + headers + "\n" +
                    "Request body: " + requestBody + "\n" +
                    "IP          : " + getClientIp(request) + "\n" +
                    "Duration    : " + duration + "ms\n" +
                    "================== Request Log End ====================";

            // 输出日志
            log.info(logMessage);

            // 复制响应内容到原始response
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

/**
 * 自定义请求包装类，用于缓存请求体
 */
class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    public byte[] getContentAsByteArray() {
        return this.cachedBody;
    }
}

/**
 * 自定义 ServletInputStream，用于读取缓存的请求体
 */
class CachedBodyServletInputStream extends ServletInputStream {
    private final InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}