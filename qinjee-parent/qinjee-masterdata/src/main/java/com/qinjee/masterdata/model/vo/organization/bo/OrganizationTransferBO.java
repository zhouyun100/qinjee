package com.qinjee.masterdata.model.vo.organization.bo;

import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 机构划转传参类
 * @author
 */
@Data
@ApiModel(description = "机构划转传参类")
public class OrganizationTransferBO implements Serializable {

    /**
     * 机构ID
     */
    @ApiModelProperty("目标机构ID")
    private Integer targetOrgId;

    /**
     * 职级
     */
    @ApiModelProperty("机构id列表")
    private List<Integer> orgIds;

    private static final long serialVersionUID = 1L;

}
