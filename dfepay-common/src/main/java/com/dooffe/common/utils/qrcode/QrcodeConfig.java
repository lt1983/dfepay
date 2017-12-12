/**
 * dooffe.com
 */
package com.dooffe.common.utils.qrcode;

import java.io.File;

/**
 * QrcodeConfig.java
 * 描述
 * @author hope
 * @version v 0.1 
 * @since 2017/07/19 上午10:30
 */
public class QrcodeConfig {
    private String contents;
    private int    width  = 400;
    private int    height = 400;
    private String format = "jpg";

    private LogoConfig logoConfig;
    private DescConfig topConfig;
    private DescConfig buttomConfig;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public LogoConfig getLogoConfig() {
        return logoConfig;
    }

    public void setLogoConfig(LogoConfig logoConfig) {
        this.logoConfig = logoConfig;
    }

    public DescConfig getTopConfig() {
        return topConfig;
    }

    public void setTopConfig(DescConfig topConfig) {
        this.topConfig = topConfig;
    }

    public DescConfig getButtomConfig() {
        return buttomConfig;
    }

    public void setButtomConfig(DescConfig buttomConfig) {
        this.buttomConfig = buttomConfig;
    }


    public static class DescConfig {
        private int fontSize = 12;
        private String desc;
        private int type;//0-顶部 1-底部

        private String fileName;

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public static  class LogoConfig {
        private File logoFile;
        private String logoFileName;
        private float logoRatio = 0.20f;

        public File getLogoFile() {
            return logoFile;
        }

        public void setLogoFile(File logoFile) {
            this.logoFile = logoFile;
        }

        public String getLogoFileName() {
            return logoFileName;
        }

        public void setLogoFileName(String logoFileName) {
            this.logoFileName = logoFileName;
        }

        public float getLogoRatio() {
            return logoRatio;
        }

        public void setLogoRatio(float logoRatio) {
            this.logoRatio = logoRatio;
        }
    }
}