package com.qinjee.masterdata.model.vo.organization.page;

import com.qinjee.masterdata.model.vo.organization.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 16:39:00
 */
@ApiModel(description = "职位分页实体Vo类")
@Data
@NoArgsConstructor
public class PositionPageVo extends PageQuery {

    @ApiModelProperty("是否删除 0未删除、1已删除")
    private Short isDelete;

    @ApiModelProperty("是否含有封存 0不含有、1含有")
    private Short isEnable;

}
