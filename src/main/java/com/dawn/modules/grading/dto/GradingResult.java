package com.dawn.modules.grading.dto;

import lombok.Data;
import java.util.List;

@Data
public class GradingResult {
    private double totalScore;
    private List<QuestionResult> questionResults;
    
    @Data
    public static class QuestionResult {
        private String questionNumber;
        private String studentAnswer;
        private String standardAnswer;
        private double score;
        private String comment;
    }
} 