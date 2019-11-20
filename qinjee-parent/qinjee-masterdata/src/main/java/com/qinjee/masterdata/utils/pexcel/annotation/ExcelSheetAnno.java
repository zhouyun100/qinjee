package com.qinjee.masterdata.utils.pexcel.annotation;

import org.apache.poi.hssf.util.HSSFColor;

import java.lang.annotation.*;

/**
 * 表信息
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelSheetAnno {

    /**
     * 表名称
     *
     * @return String
     */
    String name() default "";

    /**
     * 表头/首行的颜色
     *
     * @return HSSFColorPredefined
     */
    HSSFColor.HSSFColorPredefined headColor() default HSSFColor.HSSFColorPredefined.LIGHT_GREEN;

}

