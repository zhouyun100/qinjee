package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionLevelDao {
    int batchDelete(@Param("positionLevelIds") List<Integer> positionLevelIds);

    int insert(PositionLevel record);


    /**
     * 获取所以职级
     * @return
     */
    List<PositionLevelVo> list(Integer companyId);

    int getLastSortId(Integer companyId);

    int update(PositionLevel positionLevel);

    int sort(List<Integer> positionLevelIds, Integer operatorId);

    List<PositionLevelVo> listByPositionGradeIds(@Param("positionGradeIds") List<Integer> positionGradeIds);
    List<PositionLevelVo> listByPositionGradeId(@Param("positionGradeId") Integer positionGradeId);

    PositionLevelVo get(@Param("id") Integer id);

    PositionLevelVo getByPositionLevelName(String positionLevelName, Integer companyId);

    List<PositionLevelVo> getByPositionLevelNames(List<String> positionLevelNames, Integer companyId);

    String getPositionLevelByIdAndCompanyId(@Param("parseInt") int parseInt, @Param("companyId") Integer companyId);
}
