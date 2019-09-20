package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.model.request.UserSession;
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
}
