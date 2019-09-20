package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:22:00
 */
public interface PositionLevelService {
    /**
     * 按职位 展示职位体系
     * @param userSession
     * @return
     */
    ResponseResult<List<PositionLevel>> showByPosition(UserSession userSession);

    /**
     * 按职级 展示职位体系
     */
    void showByPositionLevel(UserSession userSession);

    /**
     * 根据职位id获取职级
     * @param positionId
     * @return
     */
    List<PositionLevel> getPositionLevelByPositionId(Integer positionId);

    /**
     * 分页查询职级列表
     * @param pageVo
     * @return
     */
    ResponseResult<PageResult<PositionLevel>> getPositionLevelList(PageVo pageVo);

    /**
     * 新增职级
     * @param positionLevelVo
     * @param userSession
     * @return
     */
    ResponseResult addPositionLevel(PositionLevelVo positionLevelVo, UserSession userSession);

    /**
     * 编辑职级
     * @param userSession
     * @param positionLevelVo
     * @return
     */
    ResponseResult editPositionLevel(UserSession userSession, PositionLevelVo positionLevelVo);

    /**
     * 删除职级
     * @param positionLevelIds
     * @param userSession
     * @return
     */
    ResponseResult deletePositionLevel(List<Integer> positionLevelIds, UserSession userSession);
}
