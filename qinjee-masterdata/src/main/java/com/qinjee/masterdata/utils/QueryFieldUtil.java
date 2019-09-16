package com.qinjee.masterdata.utils;

import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.utils.QueryColumn;
import org.apache.commons.lang3.StringUtils;

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
     * 通过查询字段Vo类获取排序字段
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
                    String value = annotation.value();
                    String fieldValue = queryField.getFieldValue();
                    //模糊查询
                    if(StringUtils.isNoneEmpty(fieldValue)){
                        queryField.setFieldValue("%" + fieldValue + "%");
                    }
                    Boolean isAscSort = queryField.getIsAscSort();
                    Boolean isFilterNull = queryField.getIsFilterNull();
                    //把查询用的别名赋值给fieldName
                    if(StringUtils.isNoneEmpty(value)){
                        queryField.setFieldName(value);
                    }

                    //是否排序
                    if(isAscSort != null){
                        if(isAscSort){
                            //正序排序
                            orderByStrs.append(value + " ASC, ");
                        }else {
                            //逆序排序
                            orderByStrs.append(value + " DESC, ");
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
