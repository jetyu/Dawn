package com.dawn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dawn")
public class DawnProperties {
    private Upload upload;
    private Ocr ocr;

    @Data
    public static class Upload {
        // 上传配置
        private String dir;
    }

    @Data
    public static class Ocr {
        // OCR服务商
        private String provider;
        // 阿里云配置
        private String aliyunAccessKeyId;
        private String aliyunAccessKeySecret;
        private String aliyunEndpoint;

    }
}