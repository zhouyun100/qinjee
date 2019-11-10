package com.qinjee.masterdata.model.vo.staff;

import javax.validation.constraints.NotNull;

public class GetFilePath {
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
}
