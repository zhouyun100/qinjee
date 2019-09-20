package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionGrade;

import java.util.List;

/**
 * @author 高雄
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
}
