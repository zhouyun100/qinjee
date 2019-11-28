package com.qinjee.utils;


import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * <p>Title: RegexpUtils</p>
 * <p>Description: </p>
 *
 * @author Lusifer
 * @version 1.0.0
 * @date 2018/6/16 23:48
 */
public class RegexpUtils {
    /**
     * 验证手机号
     */
    public static final String PHONE_REGEX = "((^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$)|(^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(17[0,1,3,5-8]))\\d{8}$))";

    /**
     * 验证邮箱地址
     */
    public static final String EMAIL_REGEX = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";

    /**
     * 验证日期时间格式
     */
    public static final String DATE_TIME_REGEX = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";

    /**
     * 验证日期格式
     */
    public static final String DATE_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";

    /**
     * 验证时间格式
     */
    public static final String TIME_REGEX = "^^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}\\s([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * 验证整数格式
     */
    public static final String NUMERIC_REGEX = "^[-\\+]?[\\d]*$";

    /**
     * 验证数字格式(限正负6位小数点)
     */
    public static final String DECIMAL_REGEX = "^(([+-]?[1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,6})?$";

    /**
     * 验证身份证格式
     */
    public static final String IDNUMBER_REGEX = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /**
     * 验证手机号
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }


    /**
     * 校验字符串是否日期格式
     * 2016-02-29               true
     * 2015-02-29               false
     * 2019/11/11 22:33:33      true
     * 2019-11-11 22:00:00      true
     * 2019-11-11 22:00         false
     * 2019-11-11 22            false
     * 2019-11                  true
     * 2019                     false
     *
     * @param strDate 待校验的字符串
     * @return
     */
    public static boolean checkDateTime(String strDate) {
        Pattern pattern = Pattern.compile(DATE_TIME_REGEX);
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验字符串是否日期格式(仅限年月日，不含时分秒)
     * 2015-02-29               true
     * 2019/11/11 22:33:33      false
     * @param strDate
     * @return
     */
    public static boolean checkDate(String strDate) {
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验字符串是否日期时间格式(仅限年、月、日、时、分、秒)
     * 2015-02-29               false
     * 2019/11/11 22:33:33      true
     * @param strDate
     * @return
     */
    public static boolean checkTime(String strDate) {
        Pattern pattern = Pattern.compile(TIME_REGEX);
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证整数格式的字符串(正整数、0、负整数)
     * @param str
     * @return
     */
    public static boolean checkInteger(String str){
        Pattern pattern = Pattern.compile(NUMERIC_REGEX);
        Matcher isNum = pattern.matcher(str);
        if(isNum.matches()){
            return true;
        }
        return false;
    }

    /**
     * 判断身份证号有效性
     * @param idNum
     * @return
     */
    public static boolean checkIdCard(String idNum) {
        Pattern pattern = Pattern.compile(IDNUMBER_REGEX);
        Matcher isNum = pattern.matcher(idNum);
        if(isNum.matches()){
            return true;
        }
        return false;
    }

    /**
     * 验证数字格式(限正负6位小数点)
     * @param input
     * @return
     */
    public static boolean checkDecimal(String input){
        Matcher mer = Pattern.compile(DECIMAL_REGEX).matcher(input);
        return mer.find();
    }

    public static void main(String [] args) {
        String str = "999999";
        String[] strArr = str.split("\\.");

        System.out.println(str+"是否合法：" + checkDecimal(str));
        System.out.println("转化后结果为：" + strArr[0].length());
        System.out.println("转化后结果为：" + strArr[1].length());
    }
}