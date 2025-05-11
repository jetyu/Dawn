package com.dawn.modules.ocr.entity;

import lombok.Data;

/**
 * OCR结构化题目实体
 */
@Data
public class OcrQuestionEntity {
    private Long id;
    private String paperId;
    private String titleId;
    private String titleType;
    private String titleContent;
    private String titleOptions;
    private String studentAnswer;
    private String correctAnswer;
    private Integer titleScore;
    private Integer studentScore;
    private Boolean answered;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

}
