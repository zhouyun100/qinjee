package com.qinjee.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码图片流
 * @date 2019/12/12
 * @author 周赟
 *
 */
public class QRCodeUtil {

    private static final int IMAGE_WIDTH = 108;
    private static final int IMAGE_HEIGHT = 108;
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
    private static final int FRAME_WIDTH = 2;
    private static MultiFormatWriter multiWriter = new MultiFormatWriter();

    /**
     * 本地上传logo文件生成二维码
     * @param logoFile logo图片文件
     * @param width 宽度
     * @param height 高度
     * @param content 二维码内容(链接地址)
     * @param os
     */
    public static void outputQRCodeImageByLogoFile(File logoFile, int width, int height, String content, OutputStream os){
        try {
            BufferedImage bufferedImage = generateQRCodeStream(null , logoFile, width, height, content);
            ImageIO.write(bufferedImage, "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 使用logoUrl生成二维码
     * @param logoUrl logo url
     * @param width 宽度
     * @param height 高度
     * @param content 二维码内容（链接地址）
     * @param os
     */
    public static void outputQRCodeImageByLogoUrl(String logoUrl, int width, int height, String content, OutputStream os){
        try {
            BufferedImage bufferedImage = generateQRCodeStream(logoUrl, null, width, height, content);
            ImageIO.write(bufferedImage, "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static BufferedImage generateQRCodeStream(String logoUrl, File logoFile, int width, int height, String content) {

        int[] pixels = new int[width * height];

        try {

            //设置图片的文字编码以及内边框
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //编码
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //边框距
            hints.put(EncodeHintType.MARGIN, 0);

            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            BitMatrix matrix = multiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /**
             * 生成二维码
             */
            for (int y = 0; y < matrix.getHeight(); y++) {
                for (int x = 0; x < matrix.getWidth(); x++) {
                    // 二维码颜色
                    int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
                    int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
                    int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
                    Color color = new Color(num1, num2, num3);
                    int colorInt = color.getRGB();
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
                }
            }

            /**
             * 设置二维码logo位置
             */
            BufferedImage scaleImage = scale(logoUrl, logoFile, IMAGE_WIDTH, IMAGE_HEIGHT, true);

            //判断logo是否存在，存在则生成带logo的二维码
            if(scaleImage != null){

                int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];

                for (int i = 0; i < scaleImage.getWidth(); i++) {
                    for (int j = 0; j < scaleImage.getHeight(); j++) {
                        srcPixels[i][j] = scaleImage.getRGB(i, j);
                    }
                }

                int halfW = matrix.getWidth() / 2;
                int halfH = matrix.getHeight() / 2;
                for (int y = 0; y < matrix.getHeight(); y++) {
                    for (int x = 0; x < matrix.getWidth(); x++) {
                        // 左上角颜色,根据自己需要调整颜色范围和颜色
                        if (x > 0 && x < 130 && y > 0 && y < 130) {
                            Color color = new Color(231, 144, 56);
                            int colorInt = color.getRGB();
                            pixels[y * width + x] = matrix.get(x, y) ? colorInt
                                    : 16777215;
                        }else if (x > halfW - IMAGE_HALF_WIDTH
                                && x < halfW + IMAGE_HALF_WIDTH
                                && y > halfH - IMAGE_HALF_WIDTH
                                && y < halfH + IMAGE_HALF_WIDTH) {
                            // 读取图片
                            pixels[y * width + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
                        } else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                                && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                                || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                                && y > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                                || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                                - IMAGE_HALF_WIDTH + FRAME_WIDTH)
                                || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                                && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
                            // 在图片四周形成边框
                            pixels[y * width + x] = 0xfffffff;
                        }
                    }
                }
            }

        }catch(WriterException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;

    }


    private static BufferedImage scale(String logoUrl, File logoFile, int width, int height, boolean hasFiller) throws Exception {
        // 缩放比例
        double ratio;
        Image destImage;

        try{
            BufferedImage srcImage;

            if(StringUtils.isNoneBlank(logoUrl)){
                URL url = new URL(logoUrl);
                InputStream logoInputStream = url.openStream();
                srcImage = ImageIO.read(logoInputStream);
            }else if(logoFile != null){
                srcImage = ImageIO.read(logoFile);
            }else{
                return null;
            }
            destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);

            // 计算比例
            if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
                if (srcImage.getHeight() > srcImage.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / srcImage.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue()
                            / srcImage.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(
                        AffineTransform.getScaleInstance(ratio, ratio), null);
                destImage = op.filter(srcImage, null);
            }
            if (hasFiller) {
                // 补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D graphic = image.createGraphics();
                graphic.setColor(Color.white);
                graphic.fillRect(0, 0, width, height);
                if (width == destImage.getWidth(null)){
                    graphic.drawImage(destImage, 0,
                            (height - destImage.getHeight(null)) / 2,
                            destImage.getWidth(null), destImage.getHeight(null),
                            Color.white, null);
                }else {
                    graphic.drawImage(destImage,
                            (width - destImage.getWidth(null)) / 2, 0,
                            destImage.getWidth(null), destImage.getHeight(null),
                            Color.white, null);
                }
                graphic.dispose();
                destImage = image;
            }

            return (BufferedImage) destImage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String [] args) throws Exception{
        String content = "https://www.baidu.com";
        String qrCodeImagePath = "E:\\QRCode\\512x512.png";
        File logoFile = new File("E:\\Logo\\108x108.png");
        String logoUrl = "http://193.112.188.180/file/company/logo/1.png";
        int width = 512;
        int height = 512;
        BufferedImage bufferedImage = generateQRCodeStream(logoUrl, null, width, height, content);
        ImageIO.write(bufferedImage, "jpg", new File(qrCodeImagePath));
        System.out.println("SUCCESS");
    }


}
