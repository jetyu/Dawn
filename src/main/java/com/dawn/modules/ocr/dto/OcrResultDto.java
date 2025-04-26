package com.dawn.modules.ocr.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record OcrResultDto(
    @NotNull Long userId,
    @NotNull Long imageId,
    Long questionId,
    @NotBlank String ocrText,
    Float confidence
) {
    public OcrResultDto {
        if (ocrText == null || ocrText.isBlank()) {
            throw new IllegalArgumentException("OCR识别文本不能为空");
        }
    }
}
