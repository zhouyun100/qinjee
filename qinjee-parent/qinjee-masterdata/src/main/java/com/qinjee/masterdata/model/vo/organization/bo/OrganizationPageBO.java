package com.qinjee.masterdata.model.vo.organization.bo;

import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 机构实体分页Vo类
 *
 * @author
 */
@Data
public class OrganizationPageBO implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private Short isEnable;//是否含有封存(启用) 0 封存、1 未封存
    private Integer orgParentId;
    //TODO 更名为 tableHeadParam
    private List<FieldValueForSearch> tableHeadParamList;

    private static final long serialVersionUID = 1L;

}
