package com.dawn.modules.ocr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@Service
@Slf4j
public class AiOcrService {

    private final ChatClient chatClient;

    public AiOcrService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * 使用AI增强的OCR识别
     * @param file 图片文件
     * @return 识别结果
     */
    public String aiOcrRecognize(File file) {
        if (file == null || !file.exists() || !file.canRead()) {
            log.error("无效的文件: {}", file);
            throw new IllegalArgumentException("文件不可用");
        }
        try {
            String result = processImageWithAi(file);
            log.info("AI处理结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("AI处理失败", e);
            throw new RuntimeException("AI处理失败: " + e.getMessage());
        }
    }

    private String processImageWithAi(File file) throws Exception {
        // 将图片转换为 Base64
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String base64Image = Base64.getEncoder().encodeToString(fileContent);

        // 构建提示词，指导AI识别试卷内容
        String prompt = """
                请分析以下试卷图片，特别注意识别题目和考生答案，并提取以下信息:
                1. 试卷标题
                2. 题目类型(主观题/客观题)
                3. 每道题的题号、内容、分值
                4. 标准答案
                5. 考生答案

                请特别注意:
                1. 区分题目和答案区域
                2. 正确识别分数和题号
                3. 精确识别标准答案和考生答案
                4. 如果无法识别某些内容，请注明"无法识别"

                图片数据（Base64）: data:image/jpeg;base64,%s
                """.formatted(base64Image);

        log.debug("发送给 AI 的提示词: {}", prompt);

        ChatResponse response = chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse();

        if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
            log.warn("AI 返回结果为空");
            return "无法识别试卷内容";
        }

        String result = response.getResult().getOutput().getContent();
        if (result == null || result.trim().isEmpty()) {
            log.warn("AI 返回内容为空或无效");
            return "无法识别试卷内容";
        }

        return result;
    }
}