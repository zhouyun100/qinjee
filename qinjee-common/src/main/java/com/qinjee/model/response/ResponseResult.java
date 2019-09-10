package com.qinjee.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 响应结果类
 */

@Data
@ToString
@NoArgsConstructor
public class ResponseResult<T>{

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
        setResuktCode(resultCode);
    }

    private void setResuktCode(ResultCode resultCode) {
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public ResponseResult(T result, ResultCode resultCode){
        this.result = result;
        setResuktCode(resultCode);
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }

}
