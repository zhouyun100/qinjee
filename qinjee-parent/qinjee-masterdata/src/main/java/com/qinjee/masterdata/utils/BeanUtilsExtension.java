package com.qinjee.masterdata.utils;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: eTalent
 * @description: Spring BeanUtils类的一些重写扩展
 * @author: penghs
 * @create: 2020-01-11 15:13
 **/

@UtilityClass
public class BeanUtilsExtension {
    /**
     * 用法：BeanUtils.copyProperties(examLifeStyle, examDetail, getNullPropertyNames(examLifeStyle));
     * 拷贝时过滤掉源object中为null的属性
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
