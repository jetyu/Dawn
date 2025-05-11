package com.dawn.modules.ocr;

import com.dawn.modules.ocr.dto.OcrRequest;
import com.dawn.modules.ocr.dto.OcrResult;

/**
 * 试卷OCR识别服务接口，支持多种大模型实现。
 */
public interface OcrService {
    /**
     * 识别单页试卷图片中的题目与考生答案，输出结构化结果。
     * @param request 输入图片请求
     * @return 结构化识别结果
     */
    OcrResult recognizeForSinglePage(OcrRequest request);
}
