package com.qinjee.masterdata.dao.organation;


import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.BusinessOrgPostPos;
import com.qinjee.masterdata.model.vo.staff.OrganzitionVo;
import com.qinjee.model.request.UserSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface OrganizationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(OrganizationVO record);

    int insertSelective(OrganizationVO record);

    OrganizationVO selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(OrganizationVO record);

    int updateByPrimaryKey(OrganizationVO record);


    /**
     * 获取用户下所有机构
     * @param archiveId
     * @param isEnable
     * @param now
     * @return
     */
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
    List<OrganizationVO> getDirectOrganizationList(@Param("organizationPageVo") OrganizationPageVo organizationPageVo,
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
    Integer UpdateIsEnableByOrgIds(@Param("orgIds") List<Integer> orgIds, @Param("isEnable") Short isEnable);

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
    List<OrganizationVO> getSingleOrganizationListByOrgIds(@Param("orgIds")List<Integer> orgIds);

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



    List<OrganizationVO> getOrganizationsByOrgIds(@Param("orgIds")List<Integer> orgIds);

    Integer selectOrgIdByName(@Param("name") String name);

    BusinessOrgPostPos selectManyId(@Param("unitName") String businessUnitName, @Param("orgName") String orgName,
                                    @Param("postName") String postName, @Param("positionName") String positionName);

    Integer sortOrganization(@Param("orgIds") LinkedList<Integer> orgIds);

    List<OrganizationVO> getOrganizationListByUserArchiveId(@Param("archiveId")Integer archiveId,@Param("now") Date now);

    List< OrganzitionVo > selectorgBycomanyIdAndUserAuth(@Param("companyId") Integer companyId, @Param("archiveId") Integer archiveId);


    List<OrganizationVO> getOrganizationGraphics(Integer archiveId, List<Integer> orgIdList, Short isEnable, Date now);

  List<OrganizationVO> getAllOrganizationByArchiveIdAndOrgId( List<Integer> orgIdList, Integer archiveId, short parseShort, Date now);

    void updateByOrgCode(OrganizationVO vo);

    void batchDelete(@Param("idList") List<Integer> idList);
}
