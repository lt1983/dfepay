/**
 * dooffe.com
 */
package com.dooffe.common.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.dooffe.common.utils.qrcode.QrcodeConfig.DescConfig;
import com.dooffe.common.utils.qrcode.QrcodeConfig.LogoConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QrCodeCreator.java
 * 描述
 * @author hope
 * @version v 0.1 
 * @since 2017/07/19 上午10:31
 */
public class QrCodeCreator {
    private static final Logger logger = LoggerFactory.getLogger(QrCodeCreator.class);
    private static int          BLACK  = 0x000000;
    private static int          WHITE  = 0xFFFFFF;

    public static void createQrCode(QrcodeConfig qrcodeConfig, String filePath) {
        createQrCode(qrcodeConfig, new File(filePath));
    }

    public static void createQrCode(QrcodeConfig qrcodeConfig, File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file));
            createQrCode(qrcodeConfig, output);
            output.flush();
        } catch (Exception e) {
            logger.error("创建二维码失败", e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                logger.error("close error:", e);
            }
        }
    }

    public static void createQrCode(QrcodeConfig qrcodeConfig, OutputStream output) {
        BitMatrix bm = createBitMatrix(qrcodeConfig);
        BufferedImage qrcodeImage = createQrCodeImage(bm);
        if (null != qrcodeConfig.getLogoConfig()) {
            attachLogo(qrcodeImage, qrcodeConfig.getLogoConfig());
        }
        if (null != qrcodeConfig.getTopConfig()) {
            qrcodeImage = attachDesc(qrcodeImage, qrcodeConfig.getTopConfig());
        }

        if (null != qrcodeConfig.getButtomConfig()) {
            qrcodeImage = attachDesc(qrcodeImage, qrcodeConfig.getButtomConfig());
        }

        try {
            ImageIO.write(qrcodeImage, qrcodeConfig.getFormat(), output);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 在以前的image上增加描述信息，并返回一个新的image
     * @param originalImage
     * @param descConfig
     * @return
     */
    private static BufferedImage attachDesc(BufferedImage originalImage, DescConfig descConfig) {
        if (StringUtils.isNotEmpty(descConfig.getFileName())) {
            return attachDescImage(originalImage, descConfig);
        }

        String desc = descConfig.getDesc();
        if (StringUtils.isEmpty(desc)) {
            logger.debug("没有描述信息需要附着在二维码");
            return originalImage;
        }

        //得到原始的宽度和高度
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        //得到描述区的大小
        Font font = new Font("宋体", Font.PLAIN, descConfig.getFontSize());
        List<String> descList = getDescList(originalImage, desc, font);
        int descHeight = getDescHeight(originalImage, desc, font);
        //计算以前的图和描述去新的Y点
        int originalY, descY;
        if (descConfig.getType() == 0) {
            originalY = descHeight;
            descY = 0;
        } else {
            originalY = 0;
            descY = originalHeight;
        }

        //计算新的高度，然后重新生成一个Image
        int newHeight = originalHeight + descHeight;
        BufferedImage newImage = new BufferedImage(originalWidth, newHeight,
            BufferedImage.TYPE_INT_RGB);
        //在新的Image上操作
        Graphics graphics = null;
        try {
            graphics = newImage.getGraphics();
            //画以前的二维码
            graphics.drawImage(originalImage, 0, originalY, null);
            //将描述区填充白色
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, descY, originalWidth, descHeight);

            //画描述文字
            graphics.setFont(font);
            graphics.setColor(new Color(0, 160, 234));

            int fontHeight = graphics.getFontMetrics(font).getHeight();
            int baseLo = graphics.getFontMetrics().getAscent();

            for (int i = 0; i < descList.size(); i++) {
                String c = descList.get(i);
                int lineWidth = graphics.getFontMetrics(font).stringWidth(c);
                int currentLineLen = 0;
                for (int j = 0; j < c.length(); j++) {
                    String z = c.substring(j, j + 1);
                    int width = graphics.getFontMetrics(font).stringWidth(z);
                    graphics.drawString(z, (originalWidth - lineWidth) / 2 + currentLineLen,
                        descY + baseLo + fontHeight * (i) + 4);
                    currentLineLen = currentLineLen + width;
                }

            }
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        return newImage;
    }

    /**
     * 如果描述是个图片，使用另一种方式添加描述
     * @param originalImage
     * @param descConfig
     * @return
     */
    private static BufferedImage attachDescImage(BufferedImage originalImage, DescConfig descConfig) {
        if (StringUtils.isEmpty(descConfig.getFileName())) {
            return originalImage;
        }
        BufferedImage descImg;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(descConfig.getFileName());
            descImg = ImageIO.read(is);
        } catch (IOException e) {
            logger.error("",e);
            return originalImage;
        }

        //得到原始的宽度和高度
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        //获取desc的宽度和高度
        int logoWidth = descImg.getWidth();
        int logoHeight = descImg.getHeight();
        //得到描述区的大小
        Double descHeightDouble = (double) logoHeight * (originalWidth / (double) logoWidth);
        int descHeight = descHeightDouble.intValue();

        //计算以前的图和描述去新的Y点
        int originalY, descY;
        if (descConfig.getType() == 0) {
            originalY = descHeight;
            descY = 0;
        } else {
            originalY = 0;
            descY = originalHeight;
        }

        //计算新的高度，然后重新生成一个Image
        int newHeight = originalHeight + descHeight;
        BufferedImage newImage = new BufferedImage(originalWidth, newHeight,
            BufferedImage.TYPE_INT_RGB);
        //在新的Image上操作
        Graphics graphics = null;
        try {
            graphics = newImage.getGraphics();
            //画以前的二维码
            graphics.drawImage(originalImage, 0, originalY, null);
            //将描述区填充白色
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, descY, originalWidth, descHeight);

            //画描述图片
            graphics.drawImage(descImg, 0, descY, originalWidth, descHeight, null);
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        return newImage;
    }

    /**
     * 获取所有描述信息的高度
     * @param qrcodeImage
     * @param desc
     * @param font
     * @return
     */
    private static int getDescHeight(BufferedImage qrcodeImage, String desc, Font font) {
        List<String> descList = getDescList(qrcodeImage, desc, font);
        Graphics graphics = null;
        int totalFontHeight;
        try {
            graphics = qrcodeImage.getGraphics();
            int fontHeight = graphics.getFontMetrics(font).getHeight();
            int baseLo = graphics.getFontMetrics().getAscent();
            totalFontHeight = fontHeight * descList.size() + baseLo;
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        return totalFontHeight;
    }

    /**
     * 将描述切分成多个字符串，每一个字符串输出一行
     * @param qrcodeImage
     * @param desc
     * @param font
     * @return
     */
    private static List<String> getDescList(BufferedImage qrcodeImage, String desc, Font font) {
        List<String> descList = new ArrayList<String>(8);
        int qrcodeWidth = qrcodeImage.getWidth();
        //计算当前设置字体的高度
        Graphics graphics = null;
        try {
            graphics = qrcodeImage.getGraphics();
            StringBuffer sb = new StringBuffer();
            int currentLineLen = 0;
            for (int i = 0; i < desc.length(); i++) {
                char c = desc.charAt(i);
                int charWidth = graphics.getFontMetrics(font).charWidth(c);
                //每一行的字符长度加起来，不应该超过二维码真正的宽度
                if (currentLineLen + charWidth > qrcodeWidth) {
                    descList.add(sb.toString());
                    sb = new StringBuffer();
                    currentLineLen = 0;
                    continue;
                }
                currentLineLen += charWidth;
                sb.append(c);
            }
            descList.add(sb.toString());
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        return descList;
    }

    /**
     * 给二维码图片附着logo
     * @param qrcodeImage
     * @param logoConfig
     */
    private static void attachLogo(BufferedImage qrcodeImage, LogoConfig logoConfig) {
        if (!needAttachLogo(logoConfig)) {
            logger.debug("不需要附着logo");
            return;
        }

        int qrcodeWidth = qrcodeImage.getWidth();
        int qrcodeHeight = qrcodeImage.getHeight();
        float ratio = logoConfig.getLogoRatio();
        Graphics logoGraphics = null;
        try {
            //获取logo的宽度和高度
            BufferedImage logoImg = null;
            if (null != logoConfig.getLogoFile()) {
                File logoFile = logoConfig.getLogoFile();
                logoImg = ImageIO.read(logoFile);
            } else if (StringUtils.isNotEmpty(logoConfig.getLogoFileName())) {
                InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(logoConfig.getLogoFileName());
                logoImg = ImageIO.read(is);
            } else {
                logger.debug("二维码信息中没有logo信息");
                return;
            }
            int logoWidth = logoImg.getWidth();
            int logoHeight = logoImg.getHeight();
            if (ratio > 0) {
                logoWidth = logoWidth > qrcodeWidth * ratio ? (int) (qrcodeWidth * ratio)
                    : logoWidth;
                logoHeight = logoHeight > qrcodeHeight * ratio ? (int) (qrcodeHeight * ratio)
                    : logoHeight;
            }
            //找到左上角的的位置
            int x = (qrcodeWidth - logoWidth) / 2;
            int y = (qrcodeHeight - logoHeight) / 2;
            logoGraphics = qrcodeImage.getGraphics();
            //将logo附着在二维码上
            logoGraphics.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (logoGraphics != null) {
                logoGraphics.dispose();
            }
        }
    }

    /**
     * 判断是否需要在二维码上附着logo
     * @param logoConfig
     * @return
     */
    private static boolean needAttachLogo(LogoConfig logoConfig) {
        if (logoConfig == null) {
            return false;
        }

        return (logoConfig.getLogoFile() != null && logoConfig.getLogoFile().exists())
               || StringUtils.isNotEmpty(logoConfig.getLogoFileName());
    }

    /**
     * 根据二维码对象创建最初的IMAGE
     * @param bm
     * @return
     */
    private static BufferedImage createQrCodeImage(BitMatrix bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bm.get(x, y) ? BLACK : WHITE);
            }
        }
        return bufferedImage;
    }

    /**
     * 生成一个二维码对象
     * @param qrcodeConfig  二维码配置
     * @return
     */
    private static BitMatrix createBitMatrix(QrcodeConfig qrcodeConfig) {
        String contents = qrcodeConfig.getContents();
        int width = qrcodeConfig.getWidth();
        int height = qrcodeConfig.getHeight();
        Map<EncodeHintType, Object> hint = new HashMap<>(8);
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = null;
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height, hint);
        } catch (WriterException e) {
            logger.error("生成二维码失败", e);
        }
        return bitMatrix;
    }
}