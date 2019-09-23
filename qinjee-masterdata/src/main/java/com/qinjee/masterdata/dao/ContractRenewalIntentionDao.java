package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.ContractRenewalIntention;

public interface ContractRenewalIntentionDao {
    int deleteByPrimaryKey(Integer renewalIntentionId);

    int insert(ContractRenewalIntention record);

    int insertSelective(ContractRenewalIntention record);

    ContractRenewalIntention selectByPrimaryKey(Integer renewalIntentionId);

    int updateByPrimaryKeySelective(ContractRenewalIntention record);

    int updateByPrimaryKey(ContractRenewalIntention record);
}