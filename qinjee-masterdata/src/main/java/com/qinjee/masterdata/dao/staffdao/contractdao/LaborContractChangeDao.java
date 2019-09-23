package com.qinjee.masterdata.dao.staffdao.contractdao;

import com.qinjee.masterdata.model.entity.LaborContractChange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface LaborContractChangeDao {
    int deleteByPrimaryKey(Integer changeId);

    int insert(LaborContractChange record);

    int insertSelective(LaborContractChange record);

    LaborContractChange selectByPrimaryKey(Integer changeId);

    int updateByPrimaryKeySelective(LaborContractChange record);

    int updateByPrimaryKey(LaborContractChange record);

    List<LaborContractChange> selectLaborContractchange(@Param("id") Integer id);
}
