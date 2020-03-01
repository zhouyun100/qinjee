package com.qinjee.utils;

import java.lang.annotation.*;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 11:35:00
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryColumn {
    String value() default "";
}
