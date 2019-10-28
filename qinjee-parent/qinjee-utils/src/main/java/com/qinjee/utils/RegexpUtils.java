package com.qinjee.utils;


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
    public static final String PHONE = "((^((0\\\\d{2,3})-)(\\\\d{7,8})(-(\\\\d{3,}))?$)|(^((13[0-9])|(15[^4,\\\\D])|(18[0-9])|(14[5,7])|(17[0,1,3,5-8]))\\\\d{8}$))";

    /**
     * 验证邮箱地址
     */
    public static final String EMAIL = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";

    /**
     * 验证手机号
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        return phone.matches(PHONE);
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return email.matches(EMAIL);
    }
}