package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 分页接口响应结果类
 * @param <T>
 */

@Data
@ToString
public class PageResponseResult<T> extends Response {

    QueryResult<T> queryResult;

    public PageResponseResult(ResultCode resultCode, QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
