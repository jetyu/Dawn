package com.dawn.modules.paper.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paper {
    private String id;
    private String name;
    private String subject;
    private String status; // 待批改、批改中、已完成
    private LocalDateTime uploadTime;
    private String filePath;
    private Double totalScore;
    private List<Question> questions;
    private String gradingResult;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String id;
        private String content;
        private String studentAnswer;
        private String correctAnswer;
        private Double score;
        private String feedback;
    }
}