package com.qinjee.masterdata.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 机构实体Vo类
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构实体Vo类")
public class OrganationVo implements Serializable {

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码", example = "01")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称", example = "勤杰")
    private String orgName;

    /**
     * 机构类型
     */
    @ApiModelProperty(value = "机构类型", example = "集团")
    private String orgType;

    /**
     * 机构父级ID
     */
    @ApiModelProperty(value = "机构父级ID", example = "0")
    private Integer orgParentId;

    /**
     * 机构负责人
     */
    @ApiModelProperty(value = "机构负责人", example = "小明")
    private Integer orgManagerId;


    private static final long serialVersionUID = 1L;

}
