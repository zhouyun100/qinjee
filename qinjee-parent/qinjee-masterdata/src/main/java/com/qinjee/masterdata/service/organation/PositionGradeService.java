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
     * 新增职等
     * @param positionGrade
     * @return
     */
    int addPositionGrade(PositionGrade positionGrade, UserSession userSession);

    /**
     * 编辑职等
     * @param positionGrade
     * @param userSession
     * @return
     */
    int editPositionGrade(PositionGrade positionGrade, UserSession userSession);

    /**
     * 删除职等
     * @param userSession
     * @param positionGradeIds
     * @return
     */
    int batchDeletePositionGrade(UserSession userSession, List<Integer> positionGradeIds);
    /**
     * 职等排序
     * @param userSession
     * @param positionGradeIds
     * @return
     */
    int sortPositionGrade(UserSession userSession, List<Integer> positionGradeIds);

    PageResult<PositionGrade> listPositionGrade(PageVo pageVo, UserSession userSession);
}
