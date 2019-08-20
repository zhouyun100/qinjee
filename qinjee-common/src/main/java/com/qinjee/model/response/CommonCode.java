package com.qinjee.model.response;

import lombok.ToString;

/**
 * 公共的响应代码枚举类
 */

@ToString
public enum CommonCode implements ResultCode{

    INVALID_PARAM(false,10003,"非法参数！"),
    SUCCESS(true,10000,"操作成功！"),
    FAIL(false,10002,"操作失败！"),
    UNAUTHENTICATED(false,10001,"此操作需要登陆系统！"),
    UNAUTHORISE(false,10004,"权限不足，无权操作！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),
    INVALID_SESSION(false,10101,"无效会话！"),
    BUSINESS_EXCEPTION(false,10100,"业务异常！"),
    REQUEST_EXCEPTION(false,10200,"请求异常，请联系开发人员！"),
    NET_EXCEPTION(false,10201,"网络异常，服务不可用，请稍后刷新重试！");

    /**
     * 操作是否成功
     */
    boolean success;

    /**
     * 操作代码
     */
    int code;

    /**
     * 提示信息
     */
    String message;

    private CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
