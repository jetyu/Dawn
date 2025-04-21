package com.dawn.modules.paper.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/grading")
public class PaperGradingController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPaper(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("subject") String subject) {
        try {
          
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "试卷上传成功");
            response.put("paperId", "generated-paper-id");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "试卷上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/grade/{paperId}")
    public ResponseEntity<?> gradePaper(@PathVariable String paperId) {
        try {
          
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("score", 95); // 示例分数
            result.put("feedback", "答案分析完成");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "批改失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/result/{paperId}")
    public ResponseEntity<?> getGradingResult(@PathVariable String paperId) {
        try {
           
            Map<String, Object> result = new HashMap<>();
            result.put("paperId", paperId);
            result.put("score", 95);
            result.put("details", new HashMap<String, Object>() {{
                put("correctCount", 18);
                put("totalCount", 20);
                put("analysis", "答题完成度高，理解深入");
            }});
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "获取结果失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}