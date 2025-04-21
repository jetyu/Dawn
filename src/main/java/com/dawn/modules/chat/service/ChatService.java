package com.dawn.modules.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dawn.constants.Constants;

@Service
public class ChatService {

    private static final Logger debugLogger = LoggerFactory.getLogger(ChatService.class);
    
    /**
     * 解析提示词
     */
    public String parsePromptText(String grade, String course, String pattern) {
        String parsePromptText;
        if (!grade.isEmpty() && !course.isEmpty()) {
            parsePromptText = Constants.PromptText.DEFAULT + Constants.PromptText.COURSE + course
                    + Constants.PromptText.GRADE + grade + Constants.PromptText.SIMPLE;
            if (pattern.equals("简洁")) {
                parsePromptText = Constants.PromptText.DEFAULT + Constants.PromptText.COURSE + course
                        + Constants.PromptText.GRADE + grade + Constants.PromptText.SIMPLE;
            } else if (pattern.equals("详细"))
                parsePromptText = Constants.PromptText.DEFAULT + Constants.PromptText.COURSE + course
                        + Constants.PromptText.GRADE + grade + Constants.PromptText.DETAIL;
        } else {
            return null;
        }
        return parsePromptText;
    }

}