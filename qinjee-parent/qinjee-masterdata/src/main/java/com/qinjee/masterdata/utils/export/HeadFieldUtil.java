package com.qinjee.masterdata.utils.export;

import java.util.HashMap;
import java.util.Map;

public class HeadFieldUtil {

    public static Map<String, String> getFieldMap() {
        Map<String, String> map = new HashMap<>();
        map.put("姓名", "user_name");
        map.put("证件号码", "id_number");
        map.put("联系电话", "phone");
        map.put("部门", "org_name");
        map.put("单位", "business_unit_name");
        map.put("岗位", "post_name");
        map.put("直接上级", "supervisor_user_name");
        map.put("合同类别", "contract_category");
        map.put("合同编号", "contract_number");
        map.put("合同期限类型", "contract_period_type");
        map.put("合同签订日期", "contract_sign_date");
        map.put("合同起始日期", "contract_begin_date");
        map.put("合同终止日期", "contract_end_date");
        map.put("合同期限", "contract_period_month");
        map.put("合同主体", "contract_subject");
        map.put("签订次数", "sign_number");
        map.put("合同备注", "contract_remark");
        map.put("拉黑原因", "block_reason");
        map.put("入职部门编码", "org_code");
        map.put("入职岗位编码", "post_code");
        map.put("试用期限（月）", "probation_period");
        return map;
    }
    public static Map<String, String> getFieldCode() {
        Map<String, String> map = new HashMap<>();
        map.put("org_name", "部门");

        map.put("business_unit_name", "单位");
        map.put("post_name", "岗位");
        map.put("supervisor_user_name", "直接上级");
        map.put("contract_category", "合同类别");
        map.put("contract_number", "合同编号");
        map.put("contract_period_type","合同期限类型");
        map.put("contract_sign_date","合同签订日期");
        map.put("contract_begin_date","合同起始日期");
        map.put("contract_end_date","合同终止日期");
        map.put("contract_period_month", "合同期限");
        map.put("contract_subject", "合同主体");
        map.put("sign_number", "签订次数");
        map.put("contract_remark", "合同备注");
        map.put("block_reason", "拉黑原因");
        map.put("org_code","入职部门编码");
        map.put("post_code","入职岗位编码");
        map.put("probation_period","试用期限（月）");
        return map;
    }
}
