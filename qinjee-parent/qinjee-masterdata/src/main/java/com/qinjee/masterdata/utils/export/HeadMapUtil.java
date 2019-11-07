package com.qinjee.masterdata.utils.export;


import java.util.*;

public class HeadMapUtil {
    public static Map<String, String> getHeadForArc() {
        Map<String, String> map = new HashMap<>();
        map.put("archiveId", "档案id");
        map.put("userName", "姓名");
        map.put("employeeNumber", "工号");
        map.put("businessUnitName", "单位");
        map.put("orgName", "部门");
        map.put("postName", "岗位");
        map.put("hireDate", "入职日期");
        map.put("probationDueDate", "试用期到期时间");
        map.put("supervisorUserName", "直接上级");
        map.put("tel", "联系电话");
        map.put("employmentType", "任职类型");
        return map;
    }



    public static List<String> getHeadsByPre() {
        String[] strings = {"姓名", "手机", "入职状态", "邮箱", "应聘职位", "入职机构", "入职岗位",
                "入职日期", "放弃原因", "拉黑原因", "入职登记", "变更描述"};
        return Arrays.asList(strings);
    }

    public static List<String> getHeadsByCon(){
        String[] strings={"姓名","工号","性别","部门编码","部门","岗位编码","岗位","合同类型","合同签订日期","合同开始日期",
                "合同截止日期","合同期限(月)","签订次数","合同主体"};
        return Arrays.asList(strings);
    }


    /**
     * 这个map参数是比如getHeadForArc之类的map，通过名称获得属性
     * @param map
     * @return
     */
    public static List<String> getFieldName(Map<String, String> map) {
        return new ArrayList<>(map.keySet());
    }

}
