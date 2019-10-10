package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.PreEmploymentChange;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 */
@Repository
public interface PreEmploymentChangeDao {
    int deleteByPrimaryKey(Integer changeId);

    int insert(PreEmploymentChange record);

    int insertSelective(PreEmploymentChange record);

    PreEmploymentChange selectByPrimaryKey(Integer changeId);

    int updateByPrimaryKeySelective(PreEmploymentChange record);

    int updateByPrimaryKey(@Param("record") PreEmploymentChange record);

    int insertStatusChange(@Param("statusChangeVo") StatusChangeVo statusChangeVo);

}
