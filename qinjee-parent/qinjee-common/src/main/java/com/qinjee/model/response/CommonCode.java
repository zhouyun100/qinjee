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
    NET_EXCEPTION(false,10201,"网络异常，服务不可用，请稍后刷新重试！"),
    LOGIN_MULTIPLE_COMPANY(false, 10301, "多租户验证，请选择需要登录的租户平台！"),
    ORGANIZATION_OUT_OF_RANGE(false,10302,"本级机构长度超出范围,添加失败！"),
    POSITION_GROUP_NAME_REPEAT(false,10303,"职位族名重复,添加失败！"),
    POSITION_NAME_REPEAT(false,10304,"职位名重复,添加失败！"),
    ORG_EXIST_USER(false,10305, "该机构下存在人员信息，不允许删除!"),
    FILE_PARSING_EXCEPTION(false,10306, "导入文件解析异常,导入失败!"),
    FILE_FORMAT_ERROR(false,10307, "导入文件格式错误,导入失败!"),
    POST_DOES_NOT_EXIST(false,10308, "此岗位不存在,导入失败!"),
    TIME_FORMAT_ERROR(false,10310, "时间格式错误!"),
    FILE_EXPORT_FAILED(false,10311, "文件导出失败!"),
    FILE_PARSE_FAILED(false,10312, "文件解析失败!");

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

    public boolean success() {
        return false;
    }

    public int code() {
        return 0;
    }

    public String message() {
        return null;
    }
}
