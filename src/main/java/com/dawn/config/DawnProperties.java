package com.dawn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dawn")
public class DawnProperties {
    private PaperUpload paperUpload;
    private Ocr ocr;

    @Data
    public static class PaperUpload {
        // 试卷上传配置
        private String dir;
    }

    @Data
    public static class Ocr {
        // OCR服务商
        private String provider;
        // 百度云配置
        private String baiduApiKey;
        private String baiduSecretKey;
    }
}