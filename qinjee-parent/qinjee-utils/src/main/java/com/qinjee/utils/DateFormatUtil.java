/**
 * 文件名：DateFormatUtil
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期字符串格式转换工具类
 * @author 周赟
 * @date 2019/9/20
 */
public class DateFormatUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 字符串转日期
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date formatStrToDate(String dateStr,String dateFormat){

        if(StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat)){
            return null;
        }

        SimpleDateFormat simpleDateFormat = null;
        Date date = null;

        try {

            /**
             * 注意：SimpleDateFormat构造函数的样式与dateStr的样式必须相符
             */
            if(DATE_TIME_FORMAT.equals(dateFormat)){
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            }else if(DATE_FORMAT.equals(dateFormat)){
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            }

            date = simpleDateFormat.parse(dateStr);
        } catch(ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转字符串
     * @param date
     * @param dateFormat
     * @return
     *
     */
    public static String formatDateToStr(Date date,String dateFormat){

        if(null == date || StringUtils.isBlank(dateFormat)){
            return null;
        }

        SimpleDateFormat simpleDateFormat = null;
        String strDate = null;

        try {

            if(DATE_TIME_FORMAT.equals(dateFormat)){
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            }else if(DATE_FORMAT.equals(dateFormat)){
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            }

            strDate = simpleDateFormat.format(date);
        } catch(NullPointerException px) {
            px.printStackTrace();
        }
        return strDate;
    }
}
