package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 分页接口响应结果类
 * @param <T>
 */

@Data
@ToString
public class PageResponseData<T> extends ResponseResult {

    PageResult<T> pageResult;

    public PageResponseData(ResultCode resultCode, PageResult pageResult){
        super(resultCode);
       this.pageResult = pageResult;
    }

}
