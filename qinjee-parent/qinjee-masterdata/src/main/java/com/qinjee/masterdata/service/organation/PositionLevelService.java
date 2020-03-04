package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:22:00
 */
public interface PositionLevelService {


    /**
     * 分页查询职级列表
     * @param pageVo
     * @return
     */
    PageResult<PositionLevel> listPositionLevel(PageVo pageVo,UserSession userSession);

    /**
     * 新增职级
     * @param positionLevel
     * @param userSession
     * @return
     */
    int addPositionLevel(PositionLevel positionLevel, UserSession userSession);

    /**
     * 编辑职级
     * @param userSession
     * @param positionLevel
     * @return
     */
    int editPositionLevel(UserSession userSession, PositionLevel positionLevel);

    /**
     * 删除职级
     * @param positionLevelIds
     * @param userSession
     * @return
     */
    int batchDeletePositionLevel(List<Integer> positionLevelIds, UserSession userSession);

    /**
     * 职级排序
     * @param positionLevelIds
     * @param userSession
     * @return
     */
    int sortPositionLevel(List<Integer> positionLevelIds, UserSession userSession);

}
