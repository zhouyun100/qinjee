package com.qinjee.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应结果类
 * @author 彭洪思
 * @date 2019/10/16
 */
@Data
@ToString
@JsonInclude
public class ResponseResult<T> implements Serializable {

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


    T result;

    public ResponseResult(ResultCode resultCode){
        setResultCode(resultCode);
    }

    public void setResultCode(ResultCode resultCode) {
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public ResponseResult(T result, ResultCode resultCode){
        this.result = result;
        setResultCode(resultCode);
    }

    public ResponseResult(T result){
        this.result = result;
        setResultCode(CommonCode.SUCCESS);
    }

    public ResponseResult(){
        setResultCode(CommonCode.SUCCESS);
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }
    public static ResponseResult LOGIN_MULTIPLE_COMPANY(){
        return new ResponseResult(CommonCode.LOGIN_MULTIPLE_COMPANY);
    }

}
