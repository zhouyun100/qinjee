package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 分页接口响应结果类
 * @param <T>
 */

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult<T> queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

}
