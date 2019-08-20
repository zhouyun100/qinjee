package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 响应结果类
 * @param <T>
 */

@Data
@ToString
public class ResponseResult<T> extends Response {

    T result;

    public ResponseResult(ResultCode resultCode,T result){
        super(resultCode);
       this.result = result;
    }

    public ResponseResult(ResultCode resultCode){
        super(resultCode);
    }

}
