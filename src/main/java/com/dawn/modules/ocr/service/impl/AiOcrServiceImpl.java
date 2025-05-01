package com.dawn.modules.ocr.service.impl;

import com.dawn.modules.ocr.service.AiOcrService;
import com.dawn.modules.ocr.service.OcrRecognizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AiOcrServiceImpl implements AiOcrService {

    private final ChatClient chatClient;
    private final OcrRecognizeService ocrRecognizeService;

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
                    .chatResponse();

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
                1. 调整格式
                2. 保持原有结构
                3. 确保题目编号格式统一
                4. 确保答案标记格式统一

                OCR识别结果:
                %s
                """.formatted(ocrResult);

        try {
            ChatResponse response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .chatResponse();

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
                4. 标准答案
                5. 考生答案(如果有)

                请按照以下格式输出:
                标题: [试卷标题]
                题目1:
                - 题号: [题号]
                - 内容: [题目内容]
                - 类型: [题目类型]
                - 分值: [分值]
                - 标准答案: [标准答案]
                - 考生答案: [考生答案]

                OCR识别结果:
                %s
                """.formatted(ocrResult);
    }

    private Map<String, Object> parseAiResponse(String aiResponse) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> questions = new ArrayList<>();
        
        try {
            if (aiResponse == null || aiResponse.trim().isEmpty()) {
                log.warn("AI 响应为空，无法解析");
                return result;
            }

            // 提取标题
            Pattern titlePattern = Pattern.compile("标题:\\s*(.*?)(?=\\n|$)");
            Matcher titleMatcher = titlePattern.matcher(aiResponse);
            if (titleMatcher.find()) {
                result.put("title", titleMatcher.group(1).trim());
            }

            // 提取题目信息
            Pattern questionPattern = Pattern.compile(
                "题目(\\d+):\\s*" +
                "-\\s*题号:\\s*(.*?)\\s*" +
                "-\\s*内容:\\s*(.*?)\\s*" +
                "-\\s*类型:\\s*(.*?)\\s*" +
                "-\\s*分值:\\s*(.*?)\\s*" +
                "-\\s*标准答案:\\s*(.*?)\\s*" +
                "-\\s*考生答案:\\s*(.*?)(?=\\n\\n|$)",
                Pattern.DOTALL
            );
            
            Matcher questionMatcher = questionPattern.matcher(aiResponse);
            while (questionMatcher.find()) {
                Map<String, String> question = new HashMap<>();
                question.put("number", questionMatcher.group(1));
                question.put("questionNumber", questionMatcher.group(2));
                question.put("content", questionMatcher.group(3));
                question.put("type", questionMatcher.group(4));
                question.put("score", questionMatcher.group(5));
                question.put("standardAnswer", questionMatcher.group(6));
                question.put("studentAnswer", questionMatcher.group(7));
                questions.add(question);
            }
            
            result.put("questions", questions);
            return result;
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
            return result;
        }
    }
}