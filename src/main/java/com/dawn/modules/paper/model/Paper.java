package com.dawn.modules.paper.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "upload_papers")
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
    private String gradingResult;
}