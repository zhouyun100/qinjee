package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import java.lang.annotation.*;

/**
 * 自定义字段映射注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomFieldMapAnno {
    String value() default "";
}


