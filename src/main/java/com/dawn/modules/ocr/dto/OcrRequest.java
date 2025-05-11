package com.dawn.modules.ocr.dto;

import java.io.File;

import lombok.Data;

/**
 * OCR识别输入请求。
 */
@Data
public class OcrRequest {
    /** 试卷图片文件 */
    private File imageFile;
}
