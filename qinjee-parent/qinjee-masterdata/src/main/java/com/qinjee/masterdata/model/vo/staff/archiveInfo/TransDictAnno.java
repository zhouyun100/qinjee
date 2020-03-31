package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import java.lang.annotation.*;

/**
 * 自定义字典转换注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransDictAnno {
    String dictType();


}



