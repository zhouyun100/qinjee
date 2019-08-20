package com.qinjee.exception;


import com.qinjee.model.response.ResultCode;

/**
 *异常抛出类
 */
public class ExceptionCast {

    /**
     *抛出异常
     * @param resultCode
     */
    public static void cast(ResultCode resultCode){
        throw new BusinessException(resultCode);
    }
}
