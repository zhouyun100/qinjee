package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.OrganizationHistory;

public interface OrganizationHistoryDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(OrganizationHistory record);

    int insertSelective(OrganizationHistory record);

    OrganizationHistory selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(OrganizationHistory record);

    int updateByPrimaryKey(OrganizationHistory record);
}
