package com.qinjee.masterdata.utils;


import java.util.HashMap;
import java.util.Map;

public class HeadMapUtil {
    public static Map<String,String> transHeadList(){
        Map<String,String> map=new HashMap<>();
        map.put("archiveId","档案id");
        map.put("userName","姓名");
        map.put("employeeNumber","工号");
        map.put("businessUnitName","单位");
        map.put("orgName","部门");
        map.put("postName","岗位");
        map.put("hireDate","入职日期");
        map.put("probationDueDate","试用期到期时间");
        map.put("supervisorUserName","直接上级");
        map.put("tel","联系电话");
        map.put("employmentType","任职类型");
        return map;
    }
    public static Map<String,String> transTypeList(){
        Map<String,String> map=new HashMap<>();
        map.put("档案id","Short");
        map.put("姓名","String");
        map.put("工号","String");
        map.put("单位","String");
        map.put("部门","String");
        map.put("岗位","String");
        map.put("入职日期","Date");
        map.put("试用期到期时间","Date");
        map.put("直接上级","String");
        map.put("联系电话","String");
        map.put("任职类型","String");
        return map;
    }
    public static Map<String,String> transTypeCode(){
        Map<String,String> map=new HashMap<>();
        map.put("TEXT","String");
        map.put("CODE","String");
        map.put("NUMBER","Integer");
        map.put("DATE","Date");
        return map;
    }
}
