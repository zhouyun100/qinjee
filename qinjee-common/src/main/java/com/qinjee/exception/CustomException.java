package com.qinjee.exception;


import com.qinjee.model.response.ResultCode;

/**
 * 自定义异常类型
 *
 **/
public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }


}
