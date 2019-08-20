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

    PageResult<T> pageResult;

    public PageResponseResult(ResultCode resultCode, PageResult pageResult){
        super(resultCode);
       this.pageResult = pageResult;
    }

}
