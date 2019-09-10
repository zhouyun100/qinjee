package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.LaborContractChange;

public interface LaborContractChangeDao {
    int deleteByPrimaryKey(Integer changeId);

    int insert(LaborContractChange record);

    int insertSelective(LaborContractChange record);

    LaborContractChange selectByPrimaryKey(Integer changeId);

    int updateByPrimaryKeySelective(LaborContractChange record);

    int updateByPrimaryKey(LaborContractChange record);
}
