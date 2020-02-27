/*
 * 文件名： ResultJsonEntity.java
 * 
 * 工程名称: qinjee-entitys
 *
 * Qinjee
 *
 * 创建日期： 2019年5月15日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee.entity;

import java.io.Serializable;

/**
 * rest返回结果通用字段
 *
 * @author 周赟
 *
 * @version 
 *
 * @since 2019年5月15日
 */
public class ResultJsonEntity implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8498284663831929538L;

	private String resultCode;
	
	private String resultStatus;
	
	private Object result;

	/**
	 * @return 返回 resultCode。
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode 要设置的 resultCode。
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return 返回 resultStatus。
	 */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
	 * @param resultStatus 要设置的 resultStatus。
	 */
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * @return 返回 result。
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result 要设置的 result。
	 */
	public void setResult(Object result) {
		this.result = result;
	}

}
