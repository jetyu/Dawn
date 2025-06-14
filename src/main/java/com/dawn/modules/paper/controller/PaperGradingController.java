package com.dawn.modules.paper.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import com.dawn.config.DawnProperties;
import com.dawn.constants.Constants.PaperStatus;
import com.dawn.modules.paper.model.Paper;
import com.dawn.modules.paper.repository.PaperRepository;
import com.dawn.modules.paper.service.PaperGradingService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/grading")
public class PaperGradingController {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperGradingService paperGradingService;

    @Autowired
    private DawnProperties dawnProperties;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPaper(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "subject") String subject) {
        try {
            String uploadDir = dawnProperties.getPaper().getUploadDir();
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dir, filename);
            file.transferTo(dest);

            // 保存试卷记录到数据库
            Paper paper = new Paper();
            paper.setId(filename); 
            paper.setName(name);
            paper.setSubject(subject);
            paper.setPath(dest.getAbsolutePath());
            paper.setUploadTime(LocalDateTime.now());
            paper.setStatus(PaperStatus.UNGRADED.name()); // 设置默认状态
            paperRepository.save(paper);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "试卷上传成功");
            response.put("paperPath", dest.getAbsolutePath());
            response.put("paperId", filename); 
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
            return ResponseEntity.ok(papers);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "获取试卷列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/grade/{paperId}")
    public ResponseEntity<?> gradePaper(@PathVariable(name = "paperId") String paperId) {
        try {
            // 执行OCR识别
            Map<String, Object> result = paperGradingService.recognizeUploadPaper(paperId, null, null);
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
            Optional<Paper> paperOpt = paperRepository.findById(paperId);
            if (paperOpt.isEmpty()) {
                throw new RuntimeException("试卷不存在");
            }
            Paper paper = paperOpt.get();
            Map<String, Object> result = new HashMap<>();
            result.put("paperId", paperId);
            result.put("score", paper.getTotalScore());
            result.put("status", paper.getStatus());
            // 可扩展返回批改详情等
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "获取结果失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}