package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 响应结果类
 * @param <T>
 */

@Data
@ToString
public class ResponseData<T> extends ResponseResult {

    T result;

    public ResponseData(ResultCode resultCode,T result){
        super(resultCode);
       this.result = result;
    }

}
