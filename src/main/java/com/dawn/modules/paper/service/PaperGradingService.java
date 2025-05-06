package com.dawn.modules.paper.service;


import com.dawn.modules.ocr.service.AiOcrService;
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
            String ocrResult;
            try {
                ocrResult = aiOcrService.aiOcrRecognize(file);
            } catch (Exception e) {
                throw new RuntimeException("AI OCR识别失败: " + e.getMessage());
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