package com.shop.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * FastJson工具类
 * 
 * @author Claude
 */
@Component
public class JsonUtils {

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, JSONWriter.Feature.WriteMapNullValue);
    }

    /**
     * 对象转JSON字符串，并格式化输出
     *
     * @param object 对象
     * @return 格式化的JSON字符串
     */
    public static String toJsonStringPretty(Object object) {
        return JSON.toJSONString(object, JSONWriter.Feature.PrettyFormat, 
                                       JSONWriter.Feature.WriteMapNullValue);
    }

    /**
     * JSON字符串转对象
     *
     * @param jsonString JSON字符串
     * @param clazz      目标类型
     * @param <T>        泛型
     * @return 转换后的对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * JSON字符串转List
     *
     * @param jsonString JSON字符串
     * @param clazz      目标类型
     * @param <T>        泛型
     * @return 转换后的List
     */
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        return JSON.parseArray(jsonString, clazz);
    }

    /**
     * JSON字符串转Map
     *
     * @param jsonString JSON字符串
     * @return 转换后的Map
     */
    public static Map<String, Object> parseMap(String jsonString) {
        return JSON.parseObject(jsonString);
    }

    /**
     * 对象转JSONObject
     *
     * @param object 对象
     * @return JSONObject
     */
    public static JSONObject toJSONObject(Object object) {
        return JSON.parseObject(toJsonString(object));
    }

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param jsonString JSON字符串
     * @return 是否为有效的JSON
     */
    public static boolean isValidJson(String jsonString) {
        try {
            JSON.parse(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 合并两个JSON对象
     *
     * @param source 源JSON对象
     * @param target 目标JSON对象
     * @return 合并后的JSON对象
     */
    public static JSONObject merge(JSONObject source, JSONObject target) {
        JSONObject result = new JSONObject();
        result.putAll(source);
        result.putAll(target);
        return result;
    }

    /**
     * 从JSON对象中安全获取值
     *
     * @param jsonObject JSON对象
     * @param key        键
     * @param defaultValue 默认值
     * @param <T>        泛型
     * @return 值
     */
    public static <T> T getValueSafely(JSONObject jsonObject, String key, T defaultValue) {
        try {
            T value = (T) jsonObject.get(key);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}