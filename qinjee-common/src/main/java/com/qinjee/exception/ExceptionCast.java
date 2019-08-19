package com.qinjee.exception;


import com.qinjee.model.response.ResultCode;


public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
