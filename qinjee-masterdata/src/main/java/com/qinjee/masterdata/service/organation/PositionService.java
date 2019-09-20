package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.vo.organization.PositionVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:32:00
 */
public interface PositionService {
    /**
     * 根据职位族id获取职位
     * @param positionGroupId
     * @return
     */
    List<Position> getPositionListByGroupId(Integer positionGroupId);

    /**
     * 分页查询职位
     * @param userSession
     * @param pageVo
     * @return
     */
    ResponseResult<PageResult<Position>> getPositionList(UserSession userSession, PageVo pageVo);

    /**
     * 新增职位
     * @param positionVo
     * @return
     */
    ResponseResult addPosition(PositionVo positionVo, UserSession userSession);

    /**
     * 编辑职位
     * @param positionVo
     * @return
     */
    ResponseResult editPosition(PositionVo positionVo, UserSession userSession);

    /**
     * 删除职位
     * @param positionIds
     * @return
     */
    ResponseResult deletePosition(List<Integer> positionIds, UserSession userSession);

    /**
     * 职位排序
     * @param prePositionId
     * @param midPositionId
     * @param nextPositionId
     * @return
     */
    ResponseResult sortPosition(Integer prePositionId, Integer midPositionId, Integer nextPositionId);
}
