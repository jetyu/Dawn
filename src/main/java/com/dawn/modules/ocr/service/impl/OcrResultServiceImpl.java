package com.dawn.modules.ocr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawn.modules.ocr.dto.OcrResultDto;
import com.dawn.modules.ocr.entity.OcrResult;
import com.dawn.modules.ocr.repository.OcrResultRepository;
import com.dawn.modules.ocr.service.OcrResultService;

@Service
public class OcrResultServiceImpl implements OcrResultService {

    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Override
    public OcrResultDto saveOcrResult(OcrResultDto dto) {
        OcrResult entity = new OcrResult();
        entity.setUserId(dto.userId());
        entity.setImageId(dto.imageId());
        entity.setQuestionId(dto.questionId());
        entity.setOcrText(dto.ocrText());
        entity.setConfidence(dto.confidence());
        ocrResultRepository.save(entity);
        return dto;
    }
}
