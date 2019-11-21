package com.qinjee.masterdata.utils.export;


import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HeadListUtil {


    //导出思路：通过业务id与表id找到对应的数据，生成一业务id为key，value为装着属性名与属性值的list的map。通过如下方法进行解析。
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
    //导入思路：对于非内置表的类，建造一个Vo类，并在数据库中建立fieldName与code的联系。通过反射设值得到list集合，然后遍历设置进大字段表中

    public static <T> List getObjectList(List<Map<String,String>> list,Class<T> tClass) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ParseException {
        List<T> tList=new ArrayList<>();
        T t = tClass.newInstance();
        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Field declaredField = t.getClass().getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                String typeName = declaredField.getGenericType().getTypeName();
                int i = typeName.lastIndexOf(".");
                String substring = typeName.substring(i+1);
                if("Date".equals(substring)){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = simpleDateFormat.parse(entry.getValue());
                    declaredField.set(t,parse);
                }
                if("Integer".equals(substring)){
                    if(entry.getValue()==null){
                        declaredField.set(t,0);
                    }
                    declaredField.set(t,Integer.parseInt(entry.getValue()));
                }
                if("String".equals(substring)){
                    declaredField.set(t,entry.getValue());
                }
            }
            tList.add(t);
        }
        return tList;
    }
}
