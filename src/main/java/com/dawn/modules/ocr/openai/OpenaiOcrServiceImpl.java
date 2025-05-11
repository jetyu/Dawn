package com.dawn.modules.ocr.openai;

import com.dawn.common.utils.ImageFileUtils;
import com.dawn.common.utils.OpenaiWebClientUtil;
import com.dawn.common.utils.OpenaiResponseUtil;
import com.dawn.modules.ocr.OcrService;
import com.dawn.modules.ocr.dto.OcrRequest;
import com.dawn.modules.ocr.dto.OcrResult;

import com.dawn.modules.ocr.mapper.OcrQuestionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 基于大模型的试卷OCR识别实现。
 */
@Service
public class OpenaiOcrServiceImpl implements OcrService {
    private final OcrQuestionMapper ocrQuestionMapper;
    private final ObjectMapper objectMapper;
    private final OpenaiWebClientUtil openaiOcrWebClientUtil;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public OpenaiOcrServiceImpl(OcrQuestionMapper ocrQuestionMapper,
                                ObjectMapper objectMapper,
                                OpenaiWebClientUtil openaiOcrWebClientUtil) {
        this.ocrQuestionMapper = ocrQuestionMapper;
        this.objectMapper = objectMapper;
        this.openaiOcrWebClientUtil = openaiOcrWebClientUtil;
    }

    /**
     * 识别单页试卷图片中的题目与考生答案，输出结构化结果。
     *
     * @param request 输入图片请求
     * @return 结构化识别结果
     */
    @Override
    public OcrResult recognizeForSinglePage(OcrRequest request) {
        // 读取图片并转Base64
        String base64Image = ImageFileUtils.fileToBase64(request.getImageFile());
        // 通过WebClient发起OCR请求
        String prompt = com.dawn.constants.Constants.PromptText.OCR_SINGLE_PAGE_PROMPT;
        Mono<String> responseMono = openaiOcrWebClientUtil.ocrImage(base64Image, prompt);
        String responseStr = responseMono.block();
        // 解析为OcrResult
        try {
            String content = OpenaiResponseUtil.extractContent(responseStr);
            OcrResult result = OpenaiResponseUtil.parseContentToObject(content, OcrResult.class);
            // 批量插入
            if (result != null && result.getQuestions() != null && !result.getQuestions().isEmpty()) {
                java.util.List<com.dawn.modules.ocr.entity.OcrQuestionEntity> entityList = new java.util.ArrayList<>();
                String paperId;
                try {
                    java.lang.reflect.Method getPaperIdMethod = request.getClass().getMethod("getPaperId");
                    paperId = (String) getPaperIdMethod.invoke(request);
                } catch (Exception ex) {
                    paperId = null;
                }
                for (OcrResult.Question q : result.getQuestions()) {
                    com.dawn.modules.ocr.entity.OcrQuestionEntity entity = new com.dawn.modules.ocr.entity.OcrQuestionEntity();
                    entity.setPaperId(paperId);
                    entity.setTitleId(q.getNumber());
                    entity.setTitleType(q.getType());
                    entity.setTitleContent(q.getContent());
                    entity.setTitleOptions(q.getOptions() != null ? objectMapper.writeValueAsString(q.getOptions()) : null);
                    entity.setStudentAnswer(q.getAnswer());
                    entity.setCorrectAnswer(q.getCorrectAnswer());
                    entity.setTitleScore(q.getTitleScore());
                    entity.setStudentScore(q.getStudentScore());
                    if (q.getAnswered() != null) {
                        entity.setAnswered(q.getAnswered());
                    } else {
                        entity.setAnswered(q.getAnswer() != null && !q.getAnswer().trim().isEmpty());
                    }
                    entityList.add(entity);
                }
                if (!entityList.isEmpty()) {
                    ocrQuestionMapper.insertBatch(entityList);
                    log.info("已批量插入OCR题目{}条", entityList.size());
                }
            }
            return result;
        } catch (Exception e) {
            log.error("OCR结果解析失败", e);
            throw new RuntimeException("OCR结果解析失败", e);
        }
    }
}
