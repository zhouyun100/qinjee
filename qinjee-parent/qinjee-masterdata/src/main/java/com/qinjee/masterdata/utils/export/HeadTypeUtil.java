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

    public static Map<String, String> transTypeCode() {
        Map<String, String> map = new HashMap<>();
        map.put("TEXT", "String");
        map.put("CODE", "String");
        map.put("NUMBER", "Integer");
        map.put("DATE", "Date");
        return map;
    }
}
