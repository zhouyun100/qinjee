package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.LaborContract;

public interface LaborContractDao {
    int deleteByPrimaryKey(Integer contractId);

    int insert(LaborContract record);

    int insertSelective(LaborContract record);

    LaborContract selectByPrimaryKey(Integer contractId);

    int updateByPrimaryKeySelective(LaborContract record);

    int updateByPrimaryKey(LaborContract record);
}