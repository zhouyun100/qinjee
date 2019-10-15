package com.qinjee.masterdata.model.vo.organization;

import com.qinjee.model.request.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:48:00
 */
@Data
@NoArgsConstructor
@ApiModel( description = "分页查询列表的Vo")
public class PageQueryVo extends PageVo {
    @ApiModelProperty(value = "需要查询的字段")
    private List<QueryFieldVo> querFieldVos;

}
