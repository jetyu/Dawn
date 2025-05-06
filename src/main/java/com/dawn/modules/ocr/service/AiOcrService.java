package com.dawn.modules.ocr.service;

import java.io.File;

public interface AiOcrService {
    /**
     * 使用AI增强的OCR识别
     * @param file 图片文件
     * @return 识别结果
     */
    String aiOcrRecognize(File file);
} 