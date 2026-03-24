package com.alumni.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class QrCodeUtils {

    /**
     * 生成二维码图片
     */
    public static BufferedImage generateImage(String content, int width, int height) {
        QrConfig config = new QrConfig(width, height);
        config.setMargin(2);
        return QrCodeUtil.generate(content, config);
    }

    /**
     * 生成 byte[]
     */
    public static byte[] generateBytes(String content, int width, int height) {
        try {
            BufferedImage image = generateImage(content, width, height);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    /**
     * 生成 Base64（前端直接 img 使用）
     */
    public static String generateBase64(String content, int width, int height) {
        byte[] bytes = generateBytes(content, width, height);
        return "data:image/png;base64," + Base64.encode(bytes);
    }

    /**
     * 生成并保存到文件
     */
    public static void generateToFile(String content,
                                      int width,
                                      int height,
                                      String filePath) {
        File file = FileUtil.file(filePath);
        QrCodeUtil.generate(content, width, height, file);
    }

    /**
     * 自定义颜色二维码
     */
    public static BufferedImage generateWithColor(String content,
                                                  int width,
                                                  int height,
                                                  Color foreColor,
                                                  Color backColor) {

        QrConfig config = new QrConfig(width, height);
        config.setForeColor(foreColor);
        config.setBackColor(backColor);
        config.setMargin(2);

        return QrCodeUtil.generate(content, config);
    }
}