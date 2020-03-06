package com.qinjee.masterdata.utils.export;

import com.qinjee.masterdata.utils.FieldToProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransMapToObeject  {
    private static List<Object> transMapToObeject(List<Map< Integer, String >> list,Object object) throws IllegalAccessException {
        List<Object> list1=new ArrayList<>();
        for (Map<Integer, String> stringMap : list) {
            for (Map.Entry<Integer, String> integerStringEntry : stringMap.entrySet()) {
                    Class aclass = object.getClass();
                    for (Field declaredField : aclass.getDeclaredFields()) {
                        declaredField.setAccessible(true);
                        if (declaredField.getName().equals(integerStringEntry.getKey())) {
                            declaredField.set(object,integerStringEntry.getValue() );
                        }
                    }
                    list1.add(object);
            }
        }
        return list1;
    }

}
