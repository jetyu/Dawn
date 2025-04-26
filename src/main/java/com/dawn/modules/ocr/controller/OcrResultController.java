package com.dawn.modules.ocr.controller;

import com.dawn.modules.ocr.dto.OcrResultDto;
import com.dawn.modules.ocr.service.OcrResultService;
import com.dawn.modules.ocr.service.OcrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
public class OcrResultController {

    @Autowired
    private OcrResultService ocrResultService;
    @Autowired
    private OcrService ocrService;

    @PostMapping("/save")
    public ResponseEntity<OcrResultDto> saveOcrResult(@RequestBody OcrResultDto dto) {
        try {
            OcrResultDto saved = ocrResultService.saveOcrResult(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            throw new IllegalArgumentException("OCR识别结果保存失败: " + ex.getMessage());
        }
    }

    @PostMapping("/recognizeAndSave")
    public ResponseEntity<OcrResultDto> recognizeAndSave(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "questionId", required = false) Long questionId) {
        try {
            // 1. 调用OCR服务识别
            String ocrText = ocrService.recognize(file);
            // 2. 保存识别结果
            OcrResultDto dto = new OcrResultDto(userId, 0L, questionId, ocrText, null);
            OcrResultDto saved = ocrResultService.saveOcrResult(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            throw new IllegalArgumentException("OCR识别失败: " + ex.getMessage());
        }
    }
}
