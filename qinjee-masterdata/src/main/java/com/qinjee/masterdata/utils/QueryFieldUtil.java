package com.qinjee.masterdata.utils;

import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.utils.DateFormatUtil;
import com.qinjee.utils.QueryColumn;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
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
    public static String getSortFieldStr(Optional<List<QueryFieldVo>> querFieldVos, Class _class) {
        if(querFieldVos.isPresent()){
            try {
                StringBuilder orderByStrs = new StringBuilder();
                List<QueryFieldVo> queryFields = querFieldVos.get();
                for (QueryFieldVo queryField : queryFields) {
                    String fieldName = queryField.getFieldName();
                    Field field = _class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    QueryColumn annotation = field.getAnnotation(QueryColumn.class);
                    String queryColumn = annotation.value();
                    String fieldValue = queryField.getFieldValue();

                    Boolean isAscSort = queryField.getIsAscSort();
                    Boolean isFilterNull = queryField.getIsFilterNull();
                    //把查询用的别名赋值给fieldName
                    if(StringUtils.isNoneEmpty(queryColumn)){
                        if (field.getGenericType().toString().equals("class java.util.Date")) {
                            //判断是否是时间范围查询
                            String condition = queryColumn;
                            Date startTime = queryField.getStartTime();
                            Date endTime = queryField.getEndTime();
                            String joinStr = " ";
                            if(startTime != null){
                               String strat = DateFormatUtil.formatDateToStr(startTime, DateFormatUtil.DATE_TIME_FORMAT);
                                condition += " >= " + strat;
                                joinStr = " and ";
                            }
                            if(endTime != null){
                                String end = DateFormatUtil.formatDateToStr(endTime, DateFormatUtil.DATE_TIME_FORMAT);
                                condition += joinStr + queryColumn + " <= " + end;
                            }
                            queryField.setCondition(condition);
                        }else if(field.getGenericType().toString().equals("class java.util.String")){
                            //模糊查询
                            if(StringUtils.isNotBlank(fieldValue)){
                                queryField.setCondition(queryColumn + " like '%" + fieldValue + "%'");
                            }
                        }else if(field.getGenericType().toString().equals("class java.util.Integer")){
                            queryField.setCondition(queryColumn + " = " + fieldValue);
                        }else if(field.getGenericType().toString().startsWith("java.util.List<java.lang.")){
                            if (field.getGenericType().toString().endsWith("Integer>")) {
                                //如果是一个Integer集合
                                queryField.setCondition(queryColumn + " in (" + fieldValue + ")");
                            }
                            if(field.getGenericType().toString().endsWith("String>")){
                                //如果是一个String集合
                                String[] split = fieldValue.split(",");
                                StringBuilder conditions = new StringBuilder();
                                for (String str : split) {
                                    if(conditions.length() <= 0){
                                        conditions.append("'").append(str).append("'");
                                    }else {
                                        conditions.append(",'").append(str).append("'");
                                    }
                                }
                                queryField.setCondition(queryColumn + " in (" + conditions.toString() + ")");
                            }
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
                orderByStrs.delete(orderByStrs.toString().lastIndexOf(", "), orderByStrs.toString().length());
                return orderByStrs.toString();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
