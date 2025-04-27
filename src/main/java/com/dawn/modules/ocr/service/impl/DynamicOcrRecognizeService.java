package com.dawn.modules.ocr.service.impl;

import com.dawn.config.DawnProperties;
import com.dawn.modules.ocr.service.OcrRecognizeService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class DynamicOcrRecognizeService implements OcrRecognizeService {
    private final Map<String, OcrRecognizeService> ocrRecognizeServiceMap;
    private final DawnProperties dawnProperties;

    public DynamicOcrRecognizeService(Map<String, OcrRecognizeService> ocrRecognizeServiceMap, DawnProperties dawnProperties) {
        this.ocrRecognizeServiceMap = ocrRecognizeServiceMap;
        this.dawnProperties = dawnProperties;
    }

    @Override
    public String recognize(File file) {
        // 百度OCR
        OcrRecognizeService service = ocrRecognizeServiceMap.get("baiduOcrService");
        if (service == null) {
            throw new IllegalStateException("未找到百度OCR实现");
        }
        return service.recognize(file);
    }
}
