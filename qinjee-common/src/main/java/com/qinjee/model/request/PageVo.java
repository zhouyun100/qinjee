package com.qinjee.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 请求参数封装类
 */

@Data
@ToString
public class PageVo {

    @ApiModelProperty(name = "pageSize", value = "每页大小", required = true, example = "10")
    Integer pageCurrent;
    @ApiModelProperty(name = "currentPage", value = "当前页", required = true, example = "1")
    Integer pageSize;
}
