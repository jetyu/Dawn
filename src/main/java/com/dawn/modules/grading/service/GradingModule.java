package com.dawn.modules.grading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dawn.modules.grading.dto.GradingResult;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class GradingModule {
    
    @Autowired
    private FileProcessingService fileProcessingService;
    
    @Autowired
    private OCRService ocrService;
    
    @Autowired
    private AnswerComparisonService answerComparisonService;
    
    /**
     * 处理试卷评分
     * @param file 上传的试卷文件（图片或PDF）
     * @return 评分结果
     */
    public GradingResult gradePaper(MultipartFile file) throws IOException {
        // 1. 文件预处理
        List<BufferedImage> images = fileProcessingService.processFile(file);
        
        // 2. OCR识别
        Map<String, String> standardAnswers = ocrService.recognizePaper(file);
        List<String> studentAnswers = ocrService.recognizeStudentAnswers(file);
        
        // 3. 答案对比和评分
        return answerComparisonService.compareAnswers(studentAnswers, standardAnswers);
    }
} 