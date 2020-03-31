package com.qinjee.masterdata.utils;

import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
    //companyId 企业id
    //fieldNameNotInside 所有有关档案表的字段名称集合
    //临时集合，用来拼接非自定义表对的字段名
    public static String getsql(Integer companyId, List < CustomFieldVO > fieldIdNotInside, List< CustomTableVO > customTableVOS) {
        List<String> custom=new ArrayList<>();
        String substring="";
        StringBuffer stringBuffer=new StringBuffer ();
        StringBuffer stringBuffer1=new StringBuffer ();
        for (CustomTableVO customTableVO : customTableVOS) {
            if (customTableVO.getIsSystemDefine ()==0) {
                Integer tableId = customTableVO.getTableId ();
                for (CustomFieldVO customFieldVO : fieldIdNotInside) {
                    if (customFieldVO.getTableId ().equals ( tableId )) {
                        Integer fieldId = customFieldVO.getFieldId ();
                         String fieldName = customFieldVO.getFieldName ();
                        custom.add ( "substring_index(SUBSTRING(t" + tableId + ".big_data,instr(t" + tableId + ".big_data,'@@" + fieldId + "@@')+LENGTH('@@" + fieldId + "@@')+1),'@@',1)as  " + fieldName + "\t" + "," );
                    }
                }
            }
        }
        for (String s : custom) {
            if( custom!=null) {
                stringBuffer.append ( s );
            }
        }
        if(!"".equals (stringBuffer.toString ())) {
            int i = stringBuffer.toString ().lastIndexOf ( "," );
            substring = stringBuffer.toString ().substring ( 0, i );
        }
        stringBuffer1.append (" FROM\n" +"t_user_archive t0 ");
        for (CustomTableVO customTableVO : customTableVOS) {
            if (customTableVO.getIsSystemDefine ()==0) {
                Integer tableId = customTableVO.getTableId ();
                String d = " LEFT JOIN t_custom_archive_table_data " + "t" + tableId + " ON t0.archive_id = t" + tableId + ".business_id and t" + tableId + ".table_id=" + tableId;

                stringBuffer1.append ( d );
            }
        }
        stringBuffer1.append (	" WHERE t0.company_id ="+companyId +" ) t");
        return substring+stringBuffer1.toString ();
    }
}
