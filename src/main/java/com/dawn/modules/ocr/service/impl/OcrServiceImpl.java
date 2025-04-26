package com.dawn.modules.ocr.service.impl;

import com.dawn.modules.ocr.service.OcrService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 
 *
 * @author 
 * @email 
 * @date 2021-07-27 15:32:55
 */
@Service
public class OcrServiceImpl implements OcrService {
    @Override
    public String recognize(MultipartFile file) {
        try {
            // 第三方API进行OCR识别
            return "模拟OCR识别文本";
        } catch (Exception e) {
            throw new RuntimeException("OCR识别失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String recognize(File file) {
        try {
            // 这里同样可调用本地Tesseract或第三方API，实际逻辑请替换
            return "模拟OCR识别文本(File版)";
        } catch (Exception e) {
            throw new RuntimeException("OCR识别失败: " + e.getMessage(), e);
        }
    }
}
