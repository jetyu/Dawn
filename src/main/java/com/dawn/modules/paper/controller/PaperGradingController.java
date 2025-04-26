package com.dawn.modules.paper.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.dawn.modules.paper.model.Paper;
import com.dawn.modules.paper.repository.PaperRepository;
import com.dawn.modules.paper.service.PaperGradingService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/grading")
@ConfigurationProperties(prefix = "paper")
public class PaperGradingController {

    private String uploadDir;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperGradingService paperGradingService;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPaper(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "subject") String subject) {
        try {
            // 自动保存文件到配置路径
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dir, filename);
            file.transferTo(dest);

            // 保存试卷记录到数据库
            Paper paper = new Paper();
            paper.setId(filename); // 用文件名做唯一ID
            paper.setName(name);
            paper.setSubject(subject);
            paper.setPath(dest.getAbsolutePath());
            paper.setUploadTime(LocalDateTime.now());
            paper.setStatus("未批改"); // 设置默认状态
            paperRepository.save(paper);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "试卷上传成功");
            response.put("paperPath", dest.getAbsolutePath());
            response.put("paperId", filename); // 用文件名做paperId
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "试卷上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listPapers() {
        try {
            List<Paper> papers = paperRepository.findAll();
            System.out.println("查询到的试卷列表: " + papers);
            return ResponseEntity.ok(papers);
        } catch (Exception e) {
            System.err.println("获取试卷列表失败: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "获取试卷列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/grade/{paperId}")
    public ResponseEntity<?> gradePaper(@PathVariable(name = "paperId") String paperId) {
        try {  
            //paperGradingService.recognizeUploadPaper(paperId);
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
    public ResponseEntity<?> getGradingResult(@PathVariable(name = "paperId") String paperId) {
        try {

            Map<String, Object> result = new HashMap<>();
            result.put("paperId", paperId);
            result.put("score", 95);
            result.put("details", new HashMap<String, Object>() {
                {
                    put("correctCount", 18);
                    put("totalCount", 20);
                    put("analysis", "答题完成度高，理解深入");
                }
            });
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "获取结果失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}