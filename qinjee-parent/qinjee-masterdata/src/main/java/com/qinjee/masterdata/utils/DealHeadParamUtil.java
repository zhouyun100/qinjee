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
            stringBuffer.append("and ");
            for (FieldValueForSearch fieldValueForSearch : tableSelectParam) {
                if("int".equalsIgnoreCase(fieldValueForSearch.getFieldType())){
                    //进行数字类型的处理
                     getIntSql(fieldValueForSearch,stringBuffer,nickName);

                }else if("code".equalsIgnoreCase(fieldValueForSearch.getFieldType())){
                    //进行代码类型的处理
                   getCodeSql(fieldValueForSearch,stringBuffer,nickName);

                }else if("string".equalsIgnoreCase(fieldValueForSearch.getFieldType())){
                    //进行字符串类型的处理
                   getStringSql(fieldValueForSearch,stringBuffer,nickName);
                }else if("date".equalsIgnoreCase(fieldValueForSearch.getFieldType())){
                    //进行日期类型的处理
                     getDateSql(fieldValueForSearch,stringBuffer,nickName);
                }
            }
            String string = stringBuffer.toString();
            int and = string.lastIndexOf("and");
            String substring = string.substring(0, and );
            return substring;
        }
        return null;
    }
    public static String getOrderSql(List<FieldValueForSearch> tableSelectParam,String nickName){
        return getOrdersql(tableSelectParam,nickName);
    }

    private static void getIntSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue = fieldValueForSearch.getFieldValue();
               if(fieldValue.get(0)!=null && !fieldValue.get(0).equals(0)){
                   stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" >= ").append(fieldValue.get(0)).append(" and ");
               }
                if(fieldValue.size()>1 && fieldValue.get(1)!=null && !fieldValue.get(1).equals(0)){
                    stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" <= ").append(fieldValue.get(1)).append(" and ");
                }
        }else{
            stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" is null and ");
        }
    }
    private static void getCodeSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue =  fieldValueForSearch.getFieldValue();
                stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" in (");
                StringBuffer stringBuffer1=new StringBuffer();
                for (Object o : fieldValue) {
                stringBuffer1.append(" '").append(o).append( " '").append(",");
                }
            String string = stringBuffer1.toString();
            int and = string.lastIndexOf(",");
            String substring = string.substring(0, and );
            stringBuffer.append(substring).append(") and ");
                }else{
            stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" is null and ");
        }
    }
    private static void getStringSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
               stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" like ").append( "'%").append(fieldValueForSearch.getFieldValue().get(0)).append( "%'").append(" and " );
        }else{
            stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" is null and ");
        }
    }
    private static void getDateSql(FieldValueForSearch fieldValueForSearch,StringBuffer stringBuffer,String nickName) {
        if (fieldValueForSearch.getFieldValue() != null) {
                List fieldValue = fieldValueForSearch.getFieldValue();
                if(fieldValue.get(0)!=null && !fieldValue.get(0).equals(0)){
                    String date1 = String.valueOf(fieldValue.get(0));
                    if(date1!=null) {
                        stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" >= ").
                                append(" ' ").append(date1).append(" ' " ).append(" and ");
                    }else{
                        ExceptionCast.cast(CommonCode.PARAM_IS_WRONG);
                    }
                }
                if(fieldValue.size()>1&&fieldValue.get(1)!=null && !fieldValue.get(1).equals(1)){
                    String date2 = String.valueOf(fieldValue.get(1));
                    if(date2!=null) {
                        stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" <= ").
                                append(" ' ").append(date2).append(" ' " ).append(" and ");
                    }else{
                        ExceptionCast.cast(CommonCode.PARAM_IS_WRONG);
                    }
                }
        }else{
            stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(" is null and ");
        }
    }
    private static String getOrdersql(List<FieldValueForSearch> tableSelectParam,String nickName){
        StringBuffer stringBuffer=new StringBuffer();
        if(CollectionUtils.isNotEmpty(tableSelectParam)) {
            for (FieldValueForSearch fieldValueForSearch : tableSelectParam) {
                if (StringUtils.isNotBlank(fieldValueForSearch.getOrderBy())) {
                    stringBuffer.append(nickName).append(fieldValueForSearch.getFieldName()).append(getOrder(fieldValueForSearch.getOrderBy())).append(",");
                }
            }
        }
        String string = stringBuffer.toString();
        if(StringUtils.isNotBlank(string)){
            int i = string.lastIndexOf(",");
            return string.substring(0,i);
        }else {
            return null;
        }
    }
    private static String getOrder(String order){
        if("升序".equals(order)){
            return " asc ";
        }else if("降序".equals(order)){
            return  " desc ";
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
