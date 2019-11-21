package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.staff.BusinessOrgPostPos;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
@Repository
public interface OrganizationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(OrganizationVO record);

    int insertSelective(OrganizationVO record);

    OrganizationVO selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(OrganizationVO record);

    int updateByPrimaryKey(OrganizationVO record);

    /**
     * 根据档案id查询所有角色所拥有的机构
     *
     * @param archiveId
     * @return
     */
    List<OrganizationVO> getAllOrganization(@Param("archiveId") Integer archiveId,
                                            @Param("isEnable") Short isEnable,
                                            @Param("roleIds") Set<Integer> roleIds,
                                            @Param("now") Date now);

    List<OrganizationVO> getAllOrganizationByArchiveId(@Param("archiveId") Integer archiveId,
                                                       @Param("isEnable") Short isEnable,
                                                       @Param("now") Date now);

    /**
     * 根据查询条件查询机构
     *
     * @param organizationPageVo
     * @param sortFieldStr
     * @param archiveId
     * @return
     */
    List<OrganizationVO> getOrganizationList(@Param("organizationPageVo") OrganizationPageVo organizationPageVo,
                                             @Param("sortFieldStr") String sortFieldStr,
                                             @Param("archiveId") Integer archiveId,
                                             @Param("now") Date now);

    /**
     * 根据是否封存查询用户下所有的机构,图形化展示
     *
     * @param archiveId
     * @param isEnable
     * @param orgCode
     * @return
     */
    List<OrganizationVO> getOrganizationGraphics(@Param("archiveId") Integer archiveId,
                                                 @Param("isEnable") Short isEnable,
                                                 @Param("orgCode") String orgCode,
                                                 @Param("now") Date now);

    /**
     * 根据code码封存或解封机构
     *
     * @param orgIds
     * @param isEnable
     * @return
     */
    int UpdateIsEnableByOrgIds(@Param("orgIds") List<Integer> orgIds, @Param("isEnable") Short isEnable);

    /**
     * 根据机构父级id查询所有的子级
     *
     * @param orgParentId
     * @return
     */
    List<OrganizationVO> getOrganizationListByParentOrgId(Integer orgParentId);

    /**
     * 根据机构id查询机构
     *
     * @param orgIds
     * @return
     */
    List<OrganizationVO> getOrganizationListByOrgIds(List<Integer> orgIds);

    /**
     * 根据机构编码查询机构
     * @param orgCode
     * @param companyId
     * @return
     */
    OrganizationVO getOrganizationByOrgCodeAndCompanyId(@Param("orgCode") String orgCode, @Param("companyId") Integer companyId);

    /**
     * 根据id查询机构名
     * @param id
     * @return
     */
    String selectOrgName(Integer id);

    /**
     * 根据档案id查询权限下的机构id
     * @param archiveId
     * @return
     */

    /**
     * 根据选择的机构id导出Excel
     * @param orgIds
     * @return
     */
    List<OrganizationVO> getOrganizationsByOrgIds(@Param("orgIds")List<Integer> orgIds);


    List<Integer> getOrgIdByCompanyId(Integer orgId);

    List<Integer> getCompanyIdByArchiveId(Integer archiveId);

    List<Integer> getCompanyIdByAuth(@Param("archiveId") Integer archiveId);

    Integer selectOrgIdByName(@Param("name") String name);

    BusinessOrgPostPos selectManyId(@Param("unitName") String businessUnitName, @Param("orgName") String orgName,
                                    @Param("postName") String postName, @Param("positionName") String positionName);



    Integer updateBatchOrganizationSortid(@Param("orgIds") LinkedList<String> orgIds);

    List<OrganizationVO> getOrganizationListByUserArchiveId(@Param("archiveId")Integer archiveId);
}
