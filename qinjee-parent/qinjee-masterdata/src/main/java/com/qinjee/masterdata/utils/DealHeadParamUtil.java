package com.qinjee.masterdata.utils;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import com.qinjee.model.response.CommonCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DealHeadParamUtil {
    public static String getWhereSql(List<FieldValueForSearch> tableSelectParam,String nickName){
        StringBuffer stringBuffer=new StringBuffer();
        if(CollectionUtils.isNotEmpty(tableSelectParam)){
            stringBuffer.append("and");
            for (FieldValueForSearch fieldValueForSearch : tableSelectParam) {
                if("int".equals(fieldValueForSearch.getFieldType())){
                    //进行数字类型的处理
                    String intSql = getIntSql(fieldValueForSearch,stringBuffer,nickName);
                    stringBuffer.append(intSql);
                }else if("code".equals(fieldValueForSearch.getFieldType())){
                    //进行代码类型的处理
                    String codeSql = getCodeSql(fieldValueForSearch,stringBuffer,nickName);
                    stringBuffer.append(codeSql);
                }else if("string".equals(fieldValueForSearch.getFieldType())){
                    //进行字符串类型的处理
                    String stringSql = getStringSql(fieldValueForSearch,stringBuffer,nickName);
                    stringBuffer.append(stringSql);
                }else if("date".equals(fieldValueForSearch.getFieldType())){
                    //进行日期类型的处理
                    String dateSql = getDateSql(fieldValueForSearch,stringBuffer,nickName);
                    stringBuffer.append(dateSql);
                }
            }
            String string = stringBuffer.toString();
            int and = string.lastIndexOf("and");
            String substring = string.substring(0, and + 1);
            return substring+getOrdersql(tableSelectParam);
        }
        return null;
    }

    private static String getIntSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue = fieldValueForSearch.getFieldValue();
               if(fieldValue.get(0)!=null && !fieldValue.get(0).equals(0)){
                   stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("<![CDATA[ >= ]]>").append(fieldValue.get(0)).append("and");
               }
                if(fieldValue.get(1)!=null && !fieldValue.get(1).equals(1)){
                    stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("<![CDATA[ <= ]]>").append(fieldValue.get(1)).append("and");
                }
                return stringBuffer.toString();
        }
        return null;
    }
    private static String getCodeSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue = (List) fieldValueForSearch.getFieldValue();
                stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("in (");
                for (Object o : fieldValue) {
                        if(o instanceof  Integer){
                            stringBuffer.append((Integer) o);
                        }else {
                            stringBuffer.append(String.valueOf(o));
                        }
                    }
                stringBuffer.append(") and");
                return stringBuffer.toString();
                }
           return null;
    }
    private static String getStringSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
               stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("like %").append(fieldValueForSearch.getFieldValue().get(0)).append("% and");
                return stringBuffer.toString();
        }
        return null;
    }
    private static String getDateSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue = fieldValueForSearch.getFieldValue();
                if(fieldValue.get(0)!=null && !fieldValue.get(0).equals(0)){
                    String date1 = isDate(String.valueOf(fieldValue.get(0)));
                    if(date1!=null) {
                        stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("<![CDATA[ >= ]]>").append(date1).append("and");
                    }else{
                        ExceptionCast.cast(CommonCode.PARAM_IS_WRONG);
                    }
                }
                if(fieldValue.get(1)!=null && !fieldValue.get(1).equals(1)){
                    String date2 = isDate(String.valueOf(fieldValue.get(1)));
                    if(date2!=null) {
                        stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append("<![CDATA[ <= ]]>").append(date2).append("and");
                    }else{
                        ExceptionCast.cast(CommonCode.PARAM_IS_WRONG);
                    }
                }
                return stringBuffer.toString();
        }
        return null;
    }
    private static String getOrdersql(List<FieldValueForSearch> tableSelectParam){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("order by");
        for (FieldValueForSearch fieldValueForSearch : tableSelectParam) {
            if(StringUtils.isNotBlank(fieldValueForSearch.getOrderBy())){
                stringBuffer.append(fieldValueForSearch.getFieldName()).append(getOrder(fieldValueForSearch.getOrderBy()));
            }
        }
        return stringBuffer.toString();
    }
    private static String getOrder(String order){
        if("升序".equals(order)){
            return "asc";
        }else if("降序".equals(order)){
            return  "desc";
        }
        return null;
    }
    private static String isDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd" );
        try {
            return sdf.format ( new Date( date ) );
        } catch (Exception e) {
            return null;
        }
    }


}
