package com.qinjee.masterdata.utils;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

public class FieldToProperty {
    /**
     * 表字段名称转化为属性名称
     *
     * @param field
     * @return
     */
    public static final char UNDERLINE_CHAR = '_';
    public static String fieldToProperty(String field) {
        if (null == field) {
            return "";
        }
        char[] chars = field.toCharArray ();
        StringBuffer sb = new StringBuffer ();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append ( StringUtils.upperCase ( CharUtils.toString ( chars[j] ) ) );
                    i++;
                }
            } else {
                sb.append ( c );
            }
        }
        return sb.toString ();
    }
    /**
     * 驼峰转下划线
     *
     * @param camelStr
     * @return
     */
    public static String propertyTofield(String camelStr) {

        if (StringUtils.isEmpty(camelStr)) {
            return StringUtils.EMPTY;
        }

        int len = camelStr.length();
        StringBuilder strb = new StringBuilder(len + len >> 1);
        for (int i = 0; i < len; i++) {

            char c = camelStr.charAt(i);
            if (Character.isUpperCase(c)) {
                strb.append(UNDERLINE_CHAR);
                strb.append(Character.toLowerCase(c));
            } else {

                strb.append(c);
            }
        }
        return strb.toString();
    }
}
