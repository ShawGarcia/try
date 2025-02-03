package com.shop.gateway.utils;

import com.shop.common.entity.ApiResponse;
import com.shop.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

import java.io.IOException;

@UtilityClass
public class ResponseUtils {
    /**
     * 写入错误响应
     * @param response HttpServletResponse
     * @param businessCode 业务状态码
     * @param message 错误信息
     */
    public static void writeError(HttpServletResponse response, int businessCode, String message) throws IOException {
        writeResponse(response, ApiResponse.error(businessCode, message));
    }
    
    /**
     * 写入成功响应
     * @param response HttpServletResponse
     * @param data 响应数据
     */
    public static void writeSuccess(HttpServletResponse response, Object data) throws IOException {
        writeResponse(response, ApiResponse.success(data));
    }
    
    /**
     * 写入自定义响应
     * @param response HttpServletResponse
     * @param apiResponse 自定义响应对象
     */
    public static void writeResponse(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
//        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        String jsonResponse = JsonUtils.toJsonString(apiResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}