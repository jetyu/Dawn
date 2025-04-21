package com.dawn.modules.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;
import com.dawn.modules.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAiChatModel model;

    private static final Logger debugLogger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    // 构造函数注入
    public ChatController(OpenAiChatModel model) {
        this.model = model;
    }

    /**
     * 新建普通对话
     */
    @PostMapping("/newMessage")
    public ChatResponse chat(@RequestBody String message) {
        String grade = "";
        String course = "";
        String pattern = "";
        // 提示词按照用户需求进行转换。
        String parsePromptText = chatService.parsePromptText(grade, course, pattern);
        // 提示词和用户输入问题拼接。
        Prompt prompt = new Prompt(parsePromptText + message);
        // 调用模型。
        ChatResponse chatResp = model.call(prompt);
        // 返回
        return chatResp;

    }
}