package com.qinjee.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * 请求参数封装类
 */

@Data
@ToString
public class RequestData {
    Integer pageCurrent;
    Integer pageSize;
}
