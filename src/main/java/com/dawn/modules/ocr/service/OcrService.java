package com.dawn.modules.ocr.service;

import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    /**
     * 对上传的图片文件进行OCR识别，返回识别文本。
     */
    String recognize(MultipartFile file);

    /**
     * 对本地磁盘文件进行OCR识别，返回识别文本。
     */
    String recognize(java.io.File file);
}
