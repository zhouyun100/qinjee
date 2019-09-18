package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 09:39:00
 */
public interface PositionGroupService {
    /**
     * 获取所有的职位族
     * @return
     */
    List<PositionGroup> getAllPositionGroup(UserSession userSession);

    /**
     * 新增职位族
     * @param userSession
     * @param positionGroupName
     */
    ResponseResult addPositionGroup(UserSession userSession, String positionGroupName);

    /**
     * 编辑职位族
     * @param positionGroupVo
     */
    ResponseResult editPositionGroup(UserSession userSession, PositionGroupVo positionGroupVo);

    /**
     * 删除职位族
     * @param positionGroupIds
     */
    void deletePositionGroup(List<Integer> positionGroupIds);

    /**
     * 职位族排序
     * @param prePositionGroupId
     * @param midPositionGroupId
     * @param nextPositionGroupId
     * @return
     */
    ResponseResult sortPositionGroup(Integer prePositionGroupId, Integer midPositionGroupId, Integer nextPositionGroupId);

    /**
     * 职位族职位树形图展示
     * @param userSession
     * @return
     */
    ResponseResult<List<PositionGroup>> getAllPosition(UserSession userSession);
}
