package com.qinjee.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 分页查询结果封装类
 * @param <T>
 */

@Data
@ToString
public class QueryResult<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 数据总数
     */
    private long total;
}
