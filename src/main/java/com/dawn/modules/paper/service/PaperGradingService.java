package com.dawn.modules.paper.service;

import com.dawn.modules.ocr.dto.OcrRequest;
import com.dawn.modules.ocr.dto.OcrResult;
import com.dawn.constants.Constants.PaperStatus;
import com.dawn.modules.ocr.OcrService;
import com.dawn.modules.paper.repository.PaperRepository;
import com.dawn.modules.paper.model.Paper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PaperGradingService {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private OcrService ocrService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * 处理试卷OCR识别
     */
    public Map<String, Object> recognizeUploadPaper(String paperId, String name, String subject) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Paper> paperOpt = paperRepository.findById(paperId);
            if (paperOpt.isEmpty()) {
                throw new RuntimeException("试卷不存在");
            }
            Paper paper = paperOpt.get();
            String filePath = paper.getPath();
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("试卷图片文件不存在: " + filePath);
            }
            String ocrResult = null;
            double totalScore = 0;
            try {
                OcrRequest ocrRequest = new OcrRequest();
                ocrRequest.setImageFile(file);
                OcrResult ocrRes = ocrService.recognizeForSinglePage(ocrRequest);
                ocrResult = objectMapper.writeValueAsString(ocrRes);
                // 统计总分
                if (ocrRes.getQuestions() != null) {
                    for (OcrResult.Question q : ocrRes.getQuestions()) {
                        if (q.getStudentScore() != null) {
                            totalScore += q.getStudentScore();
                        }
                    }
                }
                // 更新试卷状态与分数
                paper.setStatus(PaperStatus.GRADED.name());
                paper.setTotalScore(totalScore);
                paperRepository.save(paper);
            } catch (Exception e) {
                throw new RuntimeException("AI OCR识别失败: " + e.getMessage());
            }
            result.put("status", "success");
            result.put("ocrResult", ocrResult);
            result.put("score", totalScore);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

}