package com.qinjee.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 *此工具类是将一张图片压缩到我们指定大小的范围内进行上传下载。
 */
public class ImgCompressUtil {
    /**
     * 将图片压缩到指定大小以内
     * @param maxSize 目的图片大小
     * @return 压缩后的图片数据
     */
    public static void compressUnderSize(MultipartFile multipartFile, long maxSize,File file) {
        byte[] imgBytes =null;
        try {
            imgBytes = multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        double scale = 0.9;
        byte[] imgData = Arrays.copyOf(imgBytes, imgBytes.length);
        if (imgData.length > maxSize) {
            do {
                try {
                    imgData = compress(imgData, scale);

                } catch (IOException e) {
                    throw new IllegalStateException("压缩图片过程中出错，请及时联系管理员！", e);
                }

            } while (imgData.length > maxSize);
        }
        byte2image(imgData,multipartFile,file);
    }

    /**
     * 按照 宽高 比例压缩

     * @param scale 压缩刻度
     * @return 压缩后图片数据
     * @throws IOException 压缩图片过程中出错
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
        // 源图宽度
        int width = (int) (bi.getWidth() * scale);
        // 源图高度
        int height = (int) (bi.getHeight() * scale);

        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        // 绘制处理后的图
        g.drawImage(image, 0, 0, null);
        g.dispose();

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ImageIO.write(tag, "JPEG", bOut);

        return bOut.toByteArray();
    }

    //byte数组到图片
    public static void byte2image(byte[] data,MultipartFile multipartFile,File file){
        if(data.length<3){
            return;
        }
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + multipartFile.getOriginalFilename());
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }
}
