package com.qinjee.masterdata.utils.export;

import java.util.HashMap;
import java.util.Map;

public class HeadTypeUtil {
    public static Map<String, String> getTypeForArc() {
        Map<String, String> map = new HashMap<>();
        map.put("档案id", "Short");
        map.put("姓名", "String");
        map.put("工号", "String");
        map.put("单位", "String");
        map.put("部门", "String");
        map.put("岗位", "String");
        map.put("入职日期", "Date");
        map.put("试用期到期时间", "Date");
        map.put("直接上级", "String");
        map.put("联系电话", "String");
        map.put("任职类型", "String");
        return map;
    }
    public static Map<String, String> getTypeForPre() {
        Map<String, String> map = new HashMap<>();
        map.put("姓名", "String");
        map.put("性别", "String");
        map.put("手机号", "String");
        map.put("个人邮箱", "String");
        map.put("证件类型", "String");
        map.put("证件号码", "String");
        map.put("年龄", "Short");
        map.put("参加工作时间", "Date");
        map.put("最高学历", "String");
        map.put("毕业院校", "String");
        map.put("毕业专业", "String");
        map.put("最近工作单位", "String");
        map.put("计划入职日期", "Date");
        map.put("试用期", "Short");
        map.put("应聘岗位", "String");
        map.put("入职部门编码", "String");
        map.put("入职部门名称", "String");
        map.put("入职岗位编码", "String");
        map.put("入职岗位名称", "String");
        map.put("备注", "String");
        return map;
    }

    public static Map<String, String> transTypeCode() {
        Map<String, String> map = new HashMap<>();
        map.put("TEXT", "String");
        map.put("CODE", "String");
        map.put("NUMBER", "Integer");
        map.put("DATE", "Date");
        return map;
    }
}
