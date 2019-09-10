package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TCustomOrgField;

public interface TCustomOrgFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(TCustomOrgField record);

    int insertSelective(TCustomOrgField record);

    TCustomOrgField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(TCustomOrgField record);

    int updateByPrimaryKey(TCustomOrgField record);
}