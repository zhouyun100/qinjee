package com.qinjee.masterdata.dao;


import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import org.apache.ibatis.annotations.Param;

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
    List<Organization> getAllOrganization(@Param("archiveId") Integer archiveId, @Param("isEnable") Short isEnable);

    /**
     * 根据查询条件查询机构
     * @param organizationPageVo
     * @param sortFieldStr
     * @param archiveId
     * @return
     */
    List<Organization> getOrganizationList(@Param("organizationPageVo") OrganizationPageVo organizationPageVo,
                                           @Param("sortFieldStr") String sortFieldStr,
                                           @Param("archiveId") Integer archiveId);

}
