package com.dawn.modules.ocr.service;

public interface OcrRecognizeService {
    /**
     * 对文件进行OCR识别，返回识别文本。
     */
    String recognize(java.io.File file);
}
