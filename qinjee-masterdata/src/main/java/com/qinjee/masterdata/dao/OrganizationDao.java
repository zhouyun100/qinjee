package com.qinjee.masterdata.dao;


import com.qinjee.masterdata.model.entity.Organization;

import java.util.List;

public interface OrganizationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(Organization record);

    int insertSelective(Organization record);

    Organization selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);

    /**
     * 根据档案id查询所有角色所拥有的机构
     * @param archiveId
     * @return
     */
    List<Organization> getOrganizatioList(Integer archiveId);
}
