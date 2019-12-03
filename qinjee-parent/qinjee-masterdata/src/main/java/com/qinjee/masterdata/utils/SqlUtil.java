package com.qinjee.masterdata.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
    //companyId 企业id
    //fieldNameNotInside 所有有关档案表的字段名称集合
    //临时集合，用来拼接非自定义表对的字段名
    //
    public static String getsql(Integer companyId, List<Integer> fieldNameNotInside) {
        List<String> custom=new ArrayList<>();
        StringBuffer stringBuffer=new StringBuffer();
        for (Integer integer : fieldNameNotInside) {
            if(integer!=null && !integer.equals ( 0 )) {
                custom.add("substring_index(SUBSTRING(t2.big_data,instr(t2.big_data,'@@" + integer + "@@')+LENGTH('@@" + integer + "@@')+1),';@@',1)as " + integer + "\t" + ",");
            }
        }
        for (String s : custom) {
            stringBuffer.append(s);
        }
        int i = stringBuffer.toString().lastIndexOf(",");
        String substring = stringBuffer.toString().substring(0, i);
        String c="from t_user_archive t0,t_custom_archive_table t1,t_custom_archive_table_data t2 where t0.company_id = " + companyId +"\t"+"and t1.func_code = 'ARC'"+"\t"+"and t0.company_id = t1.company_id and t2.table_id=t1.table_id"+"\t"+" and t0.archive_id = t2.business_id )t"+"\t";
        return substring+c;
    }
}
