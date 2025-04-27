package com.dawn.modules.ocr.service.impl;

import com.dawn.modules.ocr.service.OcrRecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.util.StringUtils;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Service("baiduOcrService")
public class BaiduOcrServiceImpl implements OcrRecognizeService {

    @Value("${dawn.ocr.baiduApiKey}")
    private String apiKey;

    @Value("${dawn.ocr.baiduSecretKey}")
    private String secretKey;

    private String accessToken;

    // 获取access_token
    private String fetchAccessToken() throws Exception {
        String urlStr = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
                + "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) java.net.URI.create(urlStr).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        try (InputStream in = conn.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            String response = out.toString("UTF-8");
            int start = response.indexOf("\"access_token\":\"") + 16;
            int end = response.indexOf('"', start);
            return response.substring(start, end);
        }
    }

    @Override
    public String recognize(File file) {
        try {
            if (!file.exists()) {
                throw new IllegalArgumentException("图片文件不存在: " + file.getAbsolutePath());
            }
            if (!StringUtils.hasText(accessToken)) {
                accessToken = fetchAccessToken();
            }
            byte[] imgData = Files.readAllBytes(file.toPath());
            String imgBase64 = Base64.getEncoder().encodeToString(imgData);
            String params = "image=" + java.net.URLEncoder.encode(imgBase64, "UTF-8");
            String ocrUrlStr = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token=" + accessToken;
            HttpURLConnection conn = (HttpURLConnection) java.net.URI.create(ocrUrlStr).toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            try (OutputStream out = conn.getOutputStream()) {
                out.write(params.getBytes());
            }
            try (InputStream in = conn.getInputStream(); ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    result.write(buffer, 0, len);
                }
                return result.toString("UTF-8");
            }
        } catch (Exception e) {
            log.error("百度OCR识别失败", e);
            throw new RuntimeException("百度OCR识别失败: " + e.getMessage(), e);
        }
    }
}
