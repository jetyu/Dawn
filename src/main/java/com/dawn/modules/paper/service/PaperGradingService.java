package com.dawn.modules.paper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dawn.modules.ocr.service.impl.DynamicOcrRecognizeService;
import com.dawn.modules.paper.model.Paper;
import com.dawn.modules.paper.repository.PaperRepository;

import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.util.HashMap;

@Service
public class PaperGradingService {

    @Autowired
    private DynamicOcrRecognizeService ocrRecognizeService;
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
            // 调用OCR识别服务
            String ocrResult = ocrRecognizeService.recognize(file);
            System.out.println("[OCR识别结果] " + ocrResult);
            result.put("status", "success");
            result.put("ocrResult", ocrResult);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

}