package com.dawn.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenAI接口响应通用处理工具
 */
public class OpenaiResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(OpenaiResponseUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 提取OpenAI标准响应中的content字段
     *
     * @param openaiApiResponse OpenAI接口原始响应字符串
     * @return 纯净JSON内容字符串
     */
    public static String extractContent(String openaiApiResponse) {
        try {
            JsonNode root = objectMapper.readTree(openaiApiResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            log.info("OpenAI 原始内容: {}", content);
            content = content.replaceAll("(?s)```[a-zA-Z]*|```", "").trim();
            log.info("OpenAI 格式化内容: {}", content);
            return content;
        } catch (Exception e) {
            log.error("OpenAI响应content提取失败", e);
            throw new RuntimeException("OpenAI响应content提取失败", e);
        }
    }

    /**
     * 将content字符串反序列化为目标对象
     *
     * @param content 纯净JSON内容
     * @param clazz   目标对象类型
     * @return 目标对象
     */
    public static <T> T parseContentToObject(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (Exception e) {
            log.error("OpenAI content反序列化失败", e);
            throw new RuntimeException("OpenAI content反序列化失败", e);
        }
    }
}
