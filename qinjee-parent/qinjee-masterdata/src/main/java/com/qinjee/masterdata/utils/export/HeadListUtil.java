package com.qinjee.masterdata.utils.export;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadListUtil {

    public static List<Map<String, String>> getMapList(List<T> list) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Class clazz = list.get(i).getClass();
            // 获取实体类的所有属性信息，返回Field数组
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), String.valueOf(field.get(list.get(i))));
            }
            mapList.add(map);
        }
        return mapList;
    }

    public static List<T> getObjectList(List<Map<String,String>> list, Class<T> clazz) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        List<T> list1=new ArrayList<>();
        T t= getInstance(clazz);
        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Field declaredField = t.getClass().getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                declaredField.set(t,entry.getValue());
            }
            list1.add(t);
        }
        return list1;
    }

    private static T getInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        return clazz.newInstance();
    }
}
