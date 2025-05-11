package com.dawn.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具类：通过WebClient调用OpenAI。
 */
@Component
public class OpenaiWebClientUtil {

    private final WebClient webClient;

    @Value("${dawn.ocr.openai.api-key}")
    private String apiKey;

    @Value("${dawn.ocr.openai.api-url}")
    private String apiUrl;

    @Value("${dawn.ocr.openai.model}")
    private String model;

    public OpenaiWebClientUtil(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * 发送OCR图片识别请求
     *
     * @param base64Image 图片的Base64字符串
     * @param prompt      识别提示词，可选
     * @return 识别结果JSON字符串
     */
    public Mono<String> ocrImage(String base64Image, String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", new Object[]{
                new HashMap<String, Object>() {
                    {
                        put("role", "user");
                        put("content", new Object[]{
                                new HashMap<String, Object>() {
                                    {
                                        put("type", "text");
                                        put("text", prompt != null ? prompt : "请识别图片内容");
                                    }
                                },
                                new HashMap<String, Object>() {
                                    {
                                        put("type", "image_url");
                                        put("image_url", "data:image/png;base64," + base64Image);
                                    }
                                }
                        });
                    }
                }
        });
        return webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    return Mono.error(new RuntimeException("OpenAI OCR接口调用失败: " + e.getResponseBodyAsString(), e));
                });
    }
}
