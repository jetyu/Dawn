package com.dawn.modules.grading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dawn.modules.grading.dto.GradingResult;
import com.dawn.modules.grading.service.GradingModule;

import java.io.IOException;

@RestController
@RequestMapping("/api/grading")
public class GradingController {
    
    @Autowired
    private GradingModule gradingModule;
    
    /**
     * 上传试卷进行评分
     * @param file 试卷文件（图片或PDF）
     * @return 评分结果
     */
    @PostMapping("/grade")
    public ResponseEntity<GradingResult> gradePaper(@RequestParam("file") MultipartFile file) {
        try {
            GradingResult result = gradingModule.gradePaper(file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 