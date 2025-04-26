package com.dawn.modules.ocr.service.impl;

import com.aliyun.ocr_api20210707.models.RecognizeBasicRequest;
import com.aliyun.ocr_api20210707.models.RecognizeBasicResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.ocr_api20210707.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import com.dawn.modules.ocr.service.OcrRecognizeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ocrService")
public class AliyunOcrServiceImpl implements OcrRecognizeService {

    @Value("${dawn.ocr.aliyunAccessKeyId}")
    private String accessKeyId;

    @Value("${dawn.ocr.aliyunAccessKeySecret}")
    private String accessKeySecret;

    @Value("${dawn.ocr.aliyunEndpoint}")
    private String aliyunEndpoint;

    @Override
    public String recognize(File file) {
        try {
            if (!file.exists()) {
                throw new IllegalArgumentException("图片文件不存在: " + file.getAbsolutePath());
            }
            Config config = new Config();
            config.setAccessKeyId(accessKeyId);
            config.setAccessKeySecret(accessKeySecret);
            config.setEndpoint(aliyunEndpoint);

            Client client = new Client(config);

            RecognizeBasicRequest request = new RecognizeBasicRequest();

            RecognizeBasicResponse response = client.recognizeBasic(request);

            return response.getBody().toString();
        } catch (Exception e) {
            throw new RuntimeException("阿里云OCR识别失败: " + e.getMessage(), e);
        }
    }
}
