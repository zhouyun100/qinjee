package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.model.vo.organization.PositionGradeVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:41:00
 */
public interface PositionGradeService {
    /**
     * 根据职位id获取职等
     * @param positionId
     * @return
     */
    List<PositionGrade> getPositionGradeListByPositionId(Integer positionId);

    /**
     * 分页查询职等列表
     * @param pageVo
     * @return
     */
    ResponseResult<PageResult<PositionGrade>> getPositionLevelList(PageVo pageVo);

    /**
     * 新增职等
     * @param positionGradeVo
     * @return
     */
    ResponseResult addPositionGrade(PositionGradeVo positionGradeVo, UserSession userSession);

    /**
     * 编辑职等
     * @param positionGradeVo
     * @param userSession
     * @return
     */
    ResponseResult editPositionGrade(PositionGradeVo positionGradeVo, UserSession userSession);

    /**
     * 删除职等
     * @param userSession
     * @param positionGradeIds
     * @return
     */
    ResponseResult deletePositionGrade(UserSession userSession, List<Integer> positionGradeIds);
}
