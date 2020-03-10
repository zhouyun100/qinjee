package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.entity.PositionGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PositionGradeDao {
    int batchDelete(@Param("positionGradeIds") List<Integer> positionGradeIds);

    int insert(PositionGrade record);



    int update(PositionGrade record);


    int getLastSortId(Integer companyId);
    /**
     * 查询所有职等
     * @return
     */
    List<PositionGrade> list(Integer companyId);


    int sort(Integer operatorId, List<Integer> positionGradeIds);
}
