package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.entity.PositionLevel;
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
    List<PositionLevel> list(Integer companyId);

    int getLastSortId(Integer companyId);

    int update(PositionLevel positionLevel);
}
