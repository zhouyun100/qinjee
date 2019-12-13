package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.staff.BusinessOrgPostPos;
import com.qinjee.masterdata.model.vo.staff.OrganzitionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public interface OrganizationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(OrganizationVO record);

    int insertSelective(OrganizationVO record);

    OrganizationVO getOrganizationById(Integer orgId);

    int updateOrganization(OrganizationVO record);

    int updateByPrimaryKey(OrganizationVO record);


    /**
     * 获取用户下所有机构
     *
     * @param archiveId
     * @param isEnable
     * @param now
     * @return
     */
    List<OrganizationVO> listAllOrganizationByArchiveId(@Param("archiveId") Integer archiveId,
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
    List<OrganizationVO> listDirectOrganizationByCondition(@Param("organizationPageVo") OrganizationPageVo organizationPageVo,
                                                           @Param("sortFieldStr") String sortFieldStr,
                                                           @Param("archiveId") Integer archiveId,
                                                           @Param("now") Date now);


    /**
     * 根据code码封存或解封机构
     *
     * @param orgIds
     * @param isEnable
     * @return
     */
    Integer updateEnable(@Param("orgIds") List<Integer> orgIds, @Param("isEnable") Short isEnable);

    /**
     * 根据机构父级id查询所有的子级
     *
     * @param orgParentId
     * @return
     */
    List<OrganizationVO> listSonOrganization(Integer orgParentId);

    /**
     * 根据机构id查询机构
     *
     * @param orgIds
     * @return
     */
    List<OrganizationVO> listOrgnizationByIds(@Param("orgIds") List<Integer> orgIds);

    /**
     * 根据机构编码查询机构
     *
     * @param orgCode
     * @param companyId
     * @return
     */
    OrganizationVO getOrganizationByOrgCodeAndCompanyId(@Param("orgCode") String orgCode, @Param("companyId") Integer companyId);

    /**
     * 根据id查询机构名
     *
     * @param id
     * @return
     */
    String selectOrgName(@Param("id") Integer id, @Param("companyId") Integer companyId);


    List<OrganizationVO> listOrganizationsByIds2(@Param("orgIds") List<Integer> orgIds);


    BusinessOrgPostPos selectManyId(@Param("unitName") String businessUnitName, @Param("orgName") String orgName,
                                    @Param("postName") String postName, @Param("positionName") String positionName);

    Integer sortOrganization(@Param("orgIds") LinkedList<Integer> orgIds);

    List<OrganizationVO> listOrganizationByArchiveId(@Param("archiveId") Integer archiveId, @Param("now") Date now);

    List<OrganzitionVo> getOrganizationBycomanyIdAndUserAuth(@Param("companyId") Integer companyId, @Param("archiveId") Integer archiveId);


    List<OrganizationVO> getOrganizationGraphics(Integer archiveId, List<Integer> orgIdList, Short isEnable, Date now);

    List<OrganizationVO> listAllOrganizationByArchiveIdAndOrgId(List<Integer> orgIdList, Integer archiveId, short parseShort, Date now);

    void updateByOrgCode(OrganizationVO vo);

    void batchDeleteOrganization(@Param("idList") List<Integer> idList);

    Map<String, Integer> selectOrgIdByNameAndCompanyId(@Param("orgName") String orgName, @Param("CompanyId") Integer CompanyId, @Param("postName") String postName);

    Map<String, String> getNameForOrganzition(@Param("companyId") Integer companyId, @Param("orgId") Integer orgId, @Param("postId") Integer postId);
}
