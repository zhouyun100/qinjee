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

    T queryResult;

    public ResponseResult(ResultCode resultCode,T queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
