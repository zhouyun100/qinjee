package com.qinjee.masterdata.utils;

import com.qinjee.masterdata.model.vo.organization.query.QueryField;
import com.qinjee.utils.QueryColumn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 14:39:00
 */
public class QueryFieldUtil {

    /**
     * 通过查询字段Vo类获取排序字段并处理查询条件
     * @param querFieldVos
     * @param _class
     * @return
     */
    public static String getSortFieldStr(Optional<List<QueryField>> querFieldVos, Class _class) {
        if(querFieldVos.isPresent()&&querFieldVos.get()!=null){
            try {
                StringBuilder orderByStrs = new StringBuilder();
                List<QueryField> queryFields = querFieldVos.get();
                for (QueryField queryField : queryFields) {
                    String fieldName = queryField.getFieldName();
                   if (null==fieldName||"".equals(fieldName)){
                       continue;
                   }
                    Field field = _class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    QueryColumn annotation = field.getAnnotation(QueryColumn.class);
                    if(annotation == null){
                        continue;
                    }
                    String queryColumn = annotation.value();

                    List<String> fieldValues = queryField.getFieldValues();
                    Boolean isAscSort = queryField.getIsAscSort();
                    Boolean isFilterNull = queryField.getIsFilterNull();

                    if(StringUtils.isNotBlank(queryColumn) && !CollectionUtils.isEmpty(fieldValues)){
                        queryField.setFieldName(queryColumn);
                        if (field.getType().getName().toString().equals("java.util.Date")) {
                            //判断是否是时间范围查询
                            String condition = queryColumn;
                            String startTime = fieldValues.get(0);
                            String endTime = null;
                            if(fieldValues.size() == 2){
                                endTime = fieldValues.get(1);
                            }

                            String joinStr = " ";
                            if(StringUtils.isNotBlank(startTime)){
                                condition += " >= " + startTime;
                                joinStr = " and ";
                            }
                            if(StringUtils.isNotBlank(endTime)){
                                condition += joinStr + queryColumn + " <= " + endTime;
                            }
                            queryField.setCondition(" and " + condition);
                        }else if(field.getType().getName().toString().equals("java.lang.String")){
                            //判断是否是字符串类型
                            if(fieldValues.size() == 1){
                                String fieldValue = fieldValues.get(0);
                                //模糊查询
                                if(StringUtils.isNotBlank(fieldValue)){
                                    queryField.setCondition(" and " + queryColumn + " like '%" + fieldValue + "%'");
                                }
                            }else {
                                String conditions = getConditionsString(fieldValues);
                                queryField.setCondition(" and " + queryColumn + " in (" + conditions + ")");
                            }
                        }else if(field.getType().getName().toString().equals("java.lang.Integer")){
                            //判断是否是Integer类型
                            if(fieldValues.size() == 1){
                                String fieldValue = fieldValues.get(0);
                                queryField.setCondition(" and " + queryColumn + " = " + fieldValue);
                            }else {
                                String conditions = getConditionsString(fieldValues);
                                queryField.setCondition(" and " + queryColumn + " in (" + conditions + ")");
                            }
                        }else if(field.getType().getName().toString().startsWith("java.util.List<java.lang.")){
                            //判断是否是一个集合
                            String conditionsString = getConditionsString(fieldValues);
                            //如果是一个Integer或者String集合
                            queryField.setCondition(" and " + queryColumn + " in (" + conditionsString + ")");
                        }
                    }

                    //是否排序
                    if(isAscSort != null){
                        if(isAscSort){
                            //正序排序
                            orderByStrs.append(queryColumn + " ASC, ");
                        }else {
                            //逆序排序
                            orderByStrs.append(queryColumn + " DESC, ");
                        }
                    }
                    if(isFilterNull == null){
                        queryField.setIsFilterNull(false);
                    }
                }
                if(null!=orderByStrs&&!orderByStrs.toString().equals("")&&orderByStrs.toString().endsWith(", ")){
                    orderByStrs.delete(orderByStrs.toString().lastIndexOf(", "), orderByStrs.toString().length());
                }
                return orderByStrs.toString();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 遍历查询字段集合获取查询条件
     * @param fieldValues
     * @return
     */
    private static String getConditionsString(List<String> fieldValues) {
        StringBuilder conditions = new StringBuilder();
        for (String fieldValue : fieldValues) {
            if(conditions.length() <= 0){
                conditions.append("'").append(fieldValue).append("'");
            }else {
                conditions.append(",'").append(fieldValue).append("'");
            }
        }
        return conditions.toString();
    }
}
