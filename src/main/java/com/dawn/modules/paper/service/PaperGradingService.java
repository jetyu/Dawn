package com.dawn.modules.paper.service;


import com.dawn.modules.ocr.service.AiOcrService;
import com.dawn.modules.ocr.service.impl.DynamicOcrRecognizeService;
import com.dawn.modules.paper.repository.PaperRepository;
import com.dawn.modules.paper.model.Paper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PaperGradingService {

    @Autowired
    private DynamicOcrRecognizeService ocrRecognizeService;
    @Autowired
    private AiOcrService aiOcrService;
    @Autowired
    private PaperRepository paperRepository;

    /*
     * 处理试卷OCR识别
     */
    public Map<String, Object> recognizeUploadPaper(String paperId, String name, String subject) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Paper> paperOpt = paperRepository.findById(paperId);
            if (paperOpt.isEmpty()) {
                throw new RuntimeException("试卷不存在");
            }
            Paper paper = paperOpt.get();
            String filePath = paper.getPath();
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("试卷图片文件不存在: " + filePath);
            }
            // 先尝试使用AI OCR识别
            String ocrResult;
            try {
                ocrResult = aiOcrService.recognize(file);
                log.info("[AI OCR识别结果] {}", ocrResult);
            } catch (Exception e) {
                log.warn("AI OCR识别失败，切换到百度OCR: {}", e.getMessage());
                // AI OCR失败时使用百度OCR作为备选
                ocrResult = ocrRecognizeService.recognize(file);
                log.info("[百度OCR识别结果] {}", ocrResult);
            }
            result.put("status", "success");
            result.put("ocrResult", ocrResult);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

}