package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonInclude
public class GetFilePath implements Serializable {
    /**
     * 业务模块
     */
    @NotNull
    private String 	businessModule;
    /**
     * 附件类型
     */
    @NotNull
    private String 	attachmentType;
    /**
     * 业务类型
     */
    @NotNull
    private String businessType;

    /**
     * 业务ID
     */
    @NotNull
    private Integer businessId;

    /**
     * 组名称
     */
    @NotNull
    private String groupName;
}
