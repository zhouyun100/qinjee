package com.qinjee.masterdata.utils.export;

import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.CustomFieldMapAnno;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransCustomFieldMapHelper<T> {
    public List<T> transToObeject(List<Map<String, String>> customFieldMapList, Class<T> tClass, SysDictDao sysDictDao) throws Exception {
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
                        if("code".equals(keyArr[1])){
                            String dictValue=sysDictDao.selectByCode(customFieldMap.get(key));
                            field.set(ob, dictValue);
                        }else{
                            field.set(ob, customFieldMap.get(key));
                        }
                    }
                }
            }
            resultList.add(ob);
        }
        return resultList;
    }
}
