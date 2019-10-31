package com.qinjee.masterdata.dao.staffdao.preemploymentdao;

import com.qinjee.masterdata.model.entity.PreEmploymentChange;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    int updateByPrimaryKey( PreEmploymentChange preEmploymentChange);

    int insertStatusChange(@Param("statusChangeVo") StatusChangeVo statusChangeVo);

    Integer selectIdByPreId(@Param("preEmploymentId") Integer preEmploymentId);

    List<PreEmploymentChange> selectByPreIdList(@Param("list") List<Integer> list);
}
