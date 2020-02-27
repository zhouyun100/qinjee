package com.qinjee.utils;


import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.*;


/**
 * 基于性能考虑，将所有异常抛出去，在adviceController中处理。统一写关流类。
 *
 * word文档只支持解析2003版本以后的，97版不支持
 */


/**
 * @author Administrator
 */
public class WordUtil {
    /**
     * 自定义异常类
     */
    public static class MyException  extends Exception{
        private String message;

        MyException(String message) {
            this.message = message;
        }
        @Override
        public String getMessage() {
            return message;
        }
    }
    /**
     * 关流
     * @param t 关流操作
     */
    public static <T extends Closeable> void closeStream(T t) throws Exception {
        if(null !=t){
            try {
                t.close();
            } catch (IOException e) {
                throw new MyException("关流异常");
            }
        }
    }
    /**
     * @param filePath
     * @return
     * @throws IOException
     * 获得word文档里面的内容，并输出成字符串
     */
    public static String getTextFromWord(String filePath) throws Exception  {
        String result = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new MyException("没有找到文件异常");
            }
            @SuppressWarnings("resource")
            WordExtractor wordExtractor = null;
            try {
                wordExtractor = new WordExtractor(fis);
            } catch (IOException e) {
                throw new MyException("WORD文档读取异常");
            }
            result = wordExtractor.getText();
            return result;
        } finally {
           closeStream(fis);
        }
    }
    //TODO 在生成word文档中，需要表单录入信息，然后根据表单数据来确定一个word文档模板。需要产品定义表单的录入内容以及word的呈现方式

    }


