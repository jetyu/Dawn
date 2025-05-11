package com.dawn.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * 图片文件工具类
 */
public class ImageFileUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageFileUtils.class);

    /**
     * 将图片文件转为Base64字符串
     * @param imageFile 图片文件
     * @return base64字符串
     */
    public static String fileToBase64(File imageFile) {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            log.info("图片已读取并转为Base64，长度:{}", base64Image.length());
            return base64Image;
        } catch (IOException e) {
            log.error("图片读取失败: {}", imageFile.getAbsolutePath(), e);
            throw new RuntimeException("图片读取失败: " + imageFile.getAbsolutePath(), e);
        }
    }
}
