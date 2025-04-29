package com.dawn.modules.ocr.service;

import java.io.File;
import java.util.Map;

public interface AiOcrService {
    /**
     * 使用AI增强的OCR识别
     * @param file 图片文件
     * @return 识别结果
     */
    String recognize(File file);
    
    /**
     * 使用AI分析识别结果,提取题目信息
     * @param ocrResult OCR识别结果
     * @return 结构化的题目信息
     */
    Map<String, Object> analyzeOcrResult(String ocrResult);
} 