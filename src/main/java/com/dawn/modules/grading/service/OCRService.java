package com.dawn.modules.grading.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.io.InputStream;

/**
 * OCR服务类
 */
@Service
public class OCRService {
    
    /**
     * 识别试卷内容，支持图片和PDF
     * @param file 试卷文件
     * @return 识别结果，包含题号和答案（目前只返回原始文本）
     */
    public Map<String, String> recognizePaper(MultipartFile file) throws IOException {
        String text = extractTextFromFile(file);
        // TODO: 解析text，提取题号和答案，填入Map
        Map<String, String> result = new HashMap<>();
        result.put("raw", text); // 先返回原始文本
        return result;
    }
    
    /**
     * 识别学生答案，支持图片和PDF
     * @param file 试卷文件
     * @return 学生答案列表（目前每行为一个答案）
     */
    public List<String> recognizeStudentAnswers(MultipartFile file) throws IOException {
        String text = extractTextFromFile(file);
        // 简单按行分割
        String[] lines = text.split("\\r?\\n");
        List<String> answers = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                answers.add(line.trim());
            }
        }
        return answers;
    }

    /**
     * 工具方法：从图片或PDF文件中提取文本
     */
    private String extractTextFromFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        Tesseract tesseract = new Tesseract();
        // 设置tessdata路径（请将tessdata文件夹放到 resources/tessdata 或指定绝对路径）
        tesseract.setDatapath("tessdata"); // 你可以用绝对路径，也可以放在resources下
        tesseract.setLanguage("chi_sim"); // 简体中文
        StringBuilder sb = new StringBuilder();
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            // 处理PDF：每页转图片后OCR
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFRenderer renderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); page++) {
                    BufferedImage image = renderer.renderImageWithDPI(page, 300);
                    try {
                        sb.append(tesseract.doOCR(image)).append("\n");
                    } catch (TesseractException e) {
                        throw new IOException("OCR识别失败: " + e.getMessage(), e);
                    }
                }
            }
        } else {
            // 处理图片
            try (InputStream is = file.getInputStream()) {
                BufferedImage image = ImageIO.read(is);
                if (image == null) throw new IOException("无法读取图片文件");
                try {
                    sb.append(tesseract.doOCR(image));
                } catch (TesseractException e) {
                    throw new IOException("OCR识别失败: " + e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }
}