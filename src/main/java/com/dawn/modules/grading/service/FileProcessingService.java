package com.dawn.modules.grading.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);

    /**
     * 处理上传的文件，转换为图片列表
     * 
     * @param file 上传的文件（PDF或图片）
     * @return 图片列表
     */
    public List<BufferedImage> processFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IOException("无法识别文件类型");
        }

        if (contentType.contains("pdf")) {
            return convertPdfToImages(file);
        } else if (contentType.contains("image")) {
            return List.of(processImage(file));
        } else {
            throw new IOException("不支持的文件类型: " + contentType);
        }
    }

    /**
     * 将PDF转换为图片列表
     */
    private List<BufferedImage> convertPdfToImages(MultipartFile file) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
                images.add(image);
            }
        }
        return images;
    }

    /**
     * 处理图片文件
     */
    private BufferedImage processImage(MultipartFile file) throws IOException {
        try {
            logger.debug("开始处理图片文件: {}", file.getOriginalFilename());
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                logger.error("无法读取图片文件: {}", file.getOriginalFilename());
                throw new IOException("无法读取图片文件");
            }

            // 图片预处理：调整大小、增强对比度等
            BufferedImage processedImage = preprocessImage(originalImage);
            logger.debug("图片处理完成: {}, 尺寸: {}x{}", file.getOriginalFilename(),
                    processedImage.getWidth(), processedImage.getHeight());
            return processedImage;
        } catch (IOException e) {
            logger.error("处理图片文件时发生IO异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("处理图片文件时发生未预期异常", e);
            throw new IOException("处理图片文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 图片预处理
     */
    private BufferedImage preprocessImage(BufferedImage image) {
        try {
            // 调整图片大小，确保宽度不超过2000像素
            if (image.getWidth() > 2000) {
                image = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 2000, null);
            }

            // 基本图像增强处理
            // 使用Scalr的锐化操作提高图像清晰度
            // 自定义锐化操作，兼容所有imgscalr版本
            float[] sharpenKernel = new float[] {
                    0.0f, -1.0f, 0.0f,
                    -1.0f, 5.0f, -1.0f,
                    0.0f, -1.0f, 0.0f
            };
            java.awt.image.ConvolveOp sharpenOp = new java.awt.image.ConvolveOp(
                    new java.awt.image.Kernel(3, 3, sharpenKernel));
            image = sharpenOp.filter(image, null);

            // TODO: 添加更多图片预处理步骤
            // 1. 增强对比度
            // 2. 降噪
            // 3. 二值化
            // 4. 旋转校正

            return image;
        } catch (IllegalArgumentException e) {
            // 处理参数错误异常
            logger.error("图像预处理参数错误: {}", e.getMessage());
            return image;
        } catch (OutOfMemoryError e) {
            // 处理内存不足异常
            logger.error("图像处理内存不足: {}", e.getMessage());
            // 尝试进行低质量的缩放以减少内存使用
            try {
                if (image.getWidth() > 1000) {
                    return Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 1000, null);
                }
            } catch (Exception ex) {
                // 忽略二次处理的异常
                logger.debug("低质量图像处理失败", ex);
            }
            return image;
        } catch (Exception e) {
            // 处理其他所有异常
            logger.error("图像预处理失败: {}", e.getMessage());
            return image;
        }
    }

    /**
     * 将BufferedImage转换为字节数组
     */
    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    /**
     * 将字节数组转换为BufferedImage
     */
    public BufferedImage bytesToImage(byte[] imageData) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }
}