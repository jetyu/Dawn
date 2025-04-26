package com.dawn.modules.paper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dawn.modules.ocr.service.OcrRecognizeService;
import com.dawn.modules.paper.model.Paper;
import com.dawn.modules.paper.repository.PaperRepository;
import com.dawn.modules.user.model.User;

import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.util.HashMap;

@Service
public class PaperGradingService {

    @Autowired
    private OcrRecognizeService ocrRecognizeService;
    /*
     * 处理试卷OCR识别
     */
    public Map<String, Object> recognizeUploadPaper(String paperId, String name, String subject) {
        Map<String, Object> result = new HashMap<>();
        try { 
            //Optional<Paper> paper = PaperRepository.findById(paperId).orElseThrow(() -> new RuntimeException("试卷不存在"));
            // 调用OCR识别服务
            //String ocrResult = ocrRecognizeService.recognize();
            result.put("status", "success");
            //result.put("ocrResult", ocrResult);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
 

}