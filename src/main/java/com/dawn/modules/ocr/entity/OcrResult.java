package com.dawn.modules.ocr.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ocr_result")
public class OcrResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long imageId;

    @Column
    private Long questionId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ocrText;

    @Column
    private Float confidence;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
