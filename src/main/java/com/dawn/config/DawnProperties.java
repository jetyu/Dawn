package com.dawn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dawn")
public class DawnProperties {
    private Paper paper;
    private Ocr ocr;

    @Data
    public static class Paper {
        // 试卷上传配置
        private String uploadDir;
    }

    @Data
    public static class Ocr {
        private Openai openai;

        @Data
        public static class Openai {
            /** OpenAI API Key */
            private String apiKey;
            /** OpenAI模型名称 */
            private String model;
            /** OpenAI接口地址 */
            private String apiUrl;
        }
    }
}