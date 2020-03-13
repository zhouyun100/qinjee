package com.qinjee.masterdata.model.vo.organization.bo;

import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 机构导出传参类
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "机构导出传参类")
public class OrganizationExportBO implements Serializable {



    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Integer orgId;

    /**
     * 职级
     */
    @ApiModelProperty("机构id列表")
    private List<Integer> orgIds;

    @ApiModelProperty("1:包含封存，0：不包含")
    private Short isEnable;


    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    private List<FieldValueForSearch> tableHeadParamList;

    private static final long serialVersionUID = 1L;

}
