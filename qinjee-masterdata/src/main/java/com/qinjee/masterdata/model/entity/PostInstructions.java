package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 岗位说明书表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "岗位说明书表实体类")
public class PostInstructions implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty("岗位说明书ID")
    private Integer instructionId;

    /**
     * 岗位ID
     */
    @ApiModelProperty("岗位说明书ID")
    private Integer postId;

    /**
     * 说明书附件地址
     */
    @ApiModelProperty("岗位说明书ID")
    private String instructionAttachmentUrl;

    /**
     * 操作人ID
     */
    @ApiModelProperty("岗位说明书ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty("岗位说明书ID")
    private Date createTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("岗位说明书ID")
    private Short isDelete;

    /**
     * 说明书内容
     */
    @ApiModelProperty("岗位说明书ID")
    private byte[] instructionContent;

    private static final long serialVersionUID = 1L;

}
