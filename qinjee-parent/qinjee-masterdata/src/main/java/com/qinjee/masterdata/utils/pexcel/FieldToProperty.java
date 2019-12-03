package com.qinjee.masterdata.utils.pexcel;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

public class FieldToProperty {
    /**
     * 表字段名称转化为属性名称
     *
     * @param field
     * @return
     */
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
}
