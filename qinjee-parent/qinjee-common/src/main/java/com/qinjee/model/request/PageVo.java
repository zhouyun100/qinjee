package com.qinjee.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 请求参数封装类
 */

@Data
@ToString
@ApiModel(description = "分页Vo类")
public class PageVo implements Serializable {

    @ApiModelProperty(name = "pageSize", value = "每页大小", required = true, example = "10")
    private Integer pageSize;

    @ApiModelProperty(name = "currentPage", value = "当前页", required = true, example = "1")
    private Integer currentPage;

}
