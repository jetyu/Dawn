package com.dawn.modules.ocr.service.impl;

import com.dawn.modules.ocr.service.AiOcrService;
import com.dawn.modules.ocr.service.OcrRecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AiOcrServiceImpl implements AiOcrService {

    private final ChatClient chatClient;
    private final OcrRecognizeService ocrRecognizeService;

    @Autowired
    public AiOcrServiceImpl(ChatClient.Builder chatClientBuilder,
                            @Qualifier("dynamicOcrRecognizeService") OcrRecognizeService ocrRecognizeService) {
        this.chatClient = chatClientBuilder.build();
        this.ocrRecognizeService = ocrRecognizeService;
    }

    @Override
    public String recognize(File file) {
        if (file == null || !file.exists() || !file.canRead()) {
            log.error("无效的文件: {}", file);
            throw new IllegalArgumentException("文件不可用");
        }
        try {
            String ocrResult = ocrRecognizeService.recognize(file);
            log.info("基础OCR识别结果: {}", ocrResult);
            String optimizedResult = optimizeOcrResult(ocrResult);
            log.info("AI优化后的结果: {}", optimizedResult);
            return optimizedResult;
        } catch (Exception e) {
            log.error("OCR识别失败", e);
            throw new RuntimeException("OCR识别失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeOcrResult(String ocrResult) {
        try {
            String prompt = buildAnalysisPrompt(ocrResult);
            log.info("分析提示词: {}", prompt);

            ChatResponse response = chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse(); // 获取 ChatResponse

            if (response.getResult() == null || response.getResult().getOutput() == null) {
                log.warn("AI 分析结果为空");
                return new HashMap<>();
            }
            String aiResponse = response.getResult().getOutput().getContent();
            log.info("AI分析结果: {}", aiResponse);
            return parseAiResponse(aiResponse);
        } catch (Exception e) {
            log.error("AI分析失败", e);
            throw new RuntimeException("AI分析失败: " + e.getMessage());
        }
    }

    private String optimizeOcrResult(String ocrResult) {
        String prompt = """
                请优化以下OCR识别结果,修正可能的识别错误:
                1. 修正错别字
                2. 调整格式
                3. 保持原有结构
    
                OCR识别结果:
                %s
                """.formatted(ocrResult);

        try {
            ChatResponse response = chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse(); // 获取 ChatResponse

            if (response.getResult() == null || response.getResult().getOutput() == null) {
                log.warn("AI 返回结果为空");
                return ocrResult;
            }
            return response.getResult().getOutput().getContent();
        } catch (Exception e) {
            log.error("AI 优化失败，回退到原始结果", e);
            return ocrResult;
        }
    }

    private String buildAnalysisPrompt(String ocrResult) {
        return """
                请分析以下试卷OCR识别结果,提取以下信息:
                1. 试卷标题
                2. 每道题的题号、内容、分值
                3. 题目类型(主观题/客观题)
                4. 标准答案(如果有)

                OCR识别结果:
                %s
                """.formatted(ocrResult);
    }

    private Map<String, Object> parseAiResponse(String aiResponse) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (aiResponse == null || aiResponse.trim().isEmpty()) {
                log.warn("AI 响应为空，无法解析");
                return result;
            }
            String[] lines = aiResponse.split("\n");
            for (String line : lines) {
                if (line.startsWith("标题:")) {
                    result.put("title", line.replace("标题:", "").trim());
                } else if (line.matches("\\d+\\..*")) {
                    result.put("question_" + line.split("\\.")[0], line.trim());
                }
            }
            return result;
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
            return result;
        }
    }
}