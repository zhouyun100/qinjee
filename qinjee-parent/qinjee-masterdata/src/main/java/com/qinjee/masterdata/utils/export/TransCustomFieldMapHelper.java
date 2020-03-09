package com.qinjee.masterdata.utils.export;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.CustomFieldMapAnno;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.TransDictAnno;
import com.qinjee.model.response.CommonCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransCustomFieldMapHelper<T> {
    public List<T> transToObeject(List<Map<String, String>> customFieldMapList, Class<T> tClass, List<SysDict> sysDicts) throws Exception {
        //TODO 嵌套循环太多了
        List<T> resultList = new ArrayList<>();
        for (Map<String, String> customFieldMap : customFieldMapList) {
            T ob = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                for (String key : customFieldMap.keySet()) {
                    String[] keyArr = key.split("@@");//数组有fieldCode和textType组成
                    CustomFieldMapAnno anno = field.getAnnotation(CustomFieldMapAnno.class);
                    if (keyArr[0].equals(anno.value())) {
                        field.setAccessible(true);
                        //如果fieldType是字典型，还需要转换为中文
                        if ("code".equals(keyArr[1])) {
                            List<SysDict> dicts = sysDicts.stream().filter(dict -> dict.getDictCode().equals(customFieldMap.get(key))).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(dicts)) {
                                field.set(ob, dicts.get(0).getDictValue());
                            }
                        } else {
                            field.set(ob, customFieldMap.get(key));
                        }
                    }
                }
            }
            resultList.add(ob);
        }
        return resultList;
    }

    /**
     * 单个bean 属性字典转换
     * @param ob
     * @param sysDicts
     * @throws Exception
     */
    public void transToDict(T ob, List<SysDict> sysDicts)  {
        Field[] fields = ob.getClass().getDeclaredFields();
        //找出所有带有字典转换注解的属性
        //待转换 属性集合
        List<Field> toTransFieldList = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(TransDictAnno.class)).collect(Collectors.toList());
        //进行转换
        toTransFieldList.stream().forEach(field -> {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(ob);
                if(fieldValue!=null) {
                    Optional<String> dictValue = sysDicts.stream().filter(dict -> dict.getDictCode().equals(fieldValue)).map(dict -> dict.getDictValue()).findFirst();
                    if(dictValue.isPresent()) {
                        field.set(ob, dictValue.get());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                //TODO 新增字典转换失败code
                ExceptionCast.cast(CommonCode.SERVER_ERROR);
            }
        });
    }

    /**
     * 批量bean属性字典转换
     * @param obList
     * @param sysDicts
     * @throws Exception
     */
    public void transBatchToDict(List<T> obList, List<SysDict> sysDicts)  {
        obList.stream().forEach(ob->{
            Field[] fields = ob.getClass().getDeclaredFields();
            //找出所有带有字典转换注解的属性
            //待转换 属性集合
            List<Field> toTransFieldList = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(TransDictAnno.class)).collect(Collectors.toList());
            //进行转换
            toTransFieldList.stream().forEach(field -> {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(ob);
                    if(fieldValue!=null) {
                        Optional<String> dictValue = sysDicts.stream().filter(dict -> dict.getDictCode().equals(fieldValue)).map(dict -> dict.getDictValue()).findFirst();
                        if(dictValue.isPresent()) {
                            field.set(ob, dictValue.get());
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    //TODO 新增字典转换失败注解
                    ExceptionCast.cast(CommonCode.SERVER_ERROR);
                }
            });
        });
    }
}
