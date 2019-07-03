/*
 * 文件名： KeyUtils.java
 * 
 * 工程名称: qinjee-utils
 *
 * Qinjee
 *
 * 创建日期： 2019年5月25日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee.utils;

import java.util.Random;

/**
 * 
 *
 * @author 周赟
 *
 * @version 
 *
 * @since 2019年5月25日
 */
public class KeyUtils {

	/**
	 * 
	 * 功能描述：产生独一无二的key
	 *
	 * @return
	 * 
	 * @author 周赟
	 *
	 * @since 2019年5月25日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static synchronized String genUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        String key = System.currentTimeMillis() + String.valueOf(number);
        return MD5Utils.getMd5(key);
    }
}
