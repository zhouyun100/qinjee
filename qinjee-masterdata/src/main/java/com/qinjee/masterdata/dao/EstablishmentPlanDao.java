package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.EstablishmentPlan;

public interface EstablishmentPlanDao {
    int deleteByPrimaryKey(Integer epId);

    int insert(EstablishmentPlan record);

    int insertSelective(EstablishmentPlan record);

    EstablishmentPlan selectByPrimaryKey(Integer epId);

    int updateByPrimaryKeySelective(EstablishmentPlan record);

    int updateByPrimaryKey(EstablishmentPlan record);
}