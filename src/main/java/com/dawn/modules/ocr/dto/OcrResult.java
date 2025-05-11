package com.dawn.modules.ocr.dto;

import java.util.List;

import lombok.Data;

/**
 * OCR识别结构化结果。
 */
public class OcrResult {
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * 题目信息
     */
    @Data
    public static class Question {
        private String number; // 题号
        private String type; // 题目类型（单选题、多选题、填空题、解答题）
        private String content; // 题干
        private List<String> options; // 选项（可为空）
        private String answer; // 考生答案
        private Boolean answered; // 是否作答
        private String correctAnswer; // 正确答案
        private Integer titleScore; // 题目分值
        private Integer studentScore; // 考生分值
    }
}
