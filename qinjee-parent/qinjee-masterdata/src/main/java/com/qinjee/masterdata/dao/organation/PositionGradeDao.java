package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.entity.PositionGrade;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PositionGradeDao {
    int deleteByPrimaryKey(Integer positionGradeId);

    int insert(PositionGrade record);

    int insertSelective(PositionGrade record);

    PositionGrade selectByPrimaryKey(Integer positionGradeId);

    int updateByPrimaryKeySelective(PositionGrade record);

    int updateByPrimaryKey(PositionGrade record);

    /**
     * 根据职位id获取职等
     * @param positionId
     * @return
     */
    List<PositionGrade> getPositionGradeListByPositionId(Integer positionId);

    /**
     * 查询所以职等
     * @return
     */
    List<PositionGrade> getPositionLevelList();
}
