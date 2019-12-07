package com.qinjee.masterdata.utils.export;

import java.util.HashMap;
import java.util.Map;

public class HeadFieldUtil {

    public static Map<String, String> getFieldMap() {
        Map<String, String> map = new HashMap<>();
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
        return map;
    }
}
