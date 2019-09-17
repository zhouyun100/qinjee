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

import java.security.SecureRandom;
import java.util.Random;

/**
 *  生成KEY、CODE等工具类
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

	/**
	 *
	 * 功能描述：获取随机数字验证码
	 *
	 * @return
	 *
	 * @author 周赟
	 *
	 * @since 2019年9月16日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getNonceCodeNumber(int digit) {
		if(digit > 0){
			String SYMBOLS = "0123456789";
			Random RANDOM = new SecureRandom();

			// 如果需要4位，那 new char[4] 即可，其他位数同理可得
			char[] nonceChars = new char[digit];

			for (int index = 0; index < nonceChars.length; ++index) {
				nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
			}

			return new String(nonceChars);
		}else{
			return null;
		}

	}

	public static void main(String [] args){
		System.out.println(getNonceCodeNumber(4));
	}

}
