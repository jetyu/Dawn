package com.dawn.modules.paper.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

@Data
@Entity
@Table(name = "papers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paper {
    @Id
    private String id;
    private String name;
    private String subject;
    private String path;
    private LocalDateTime uploadTime;
    private String status;
    private Double totalScore;
    @ElementCollection
    private List<Question> questions;
    private String gradingResult;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Question {
        private String id;
        private String content;
        private String studentAnswer;
        private String correctAnswer;
        private Double score;
        private String feedback;
    }
}