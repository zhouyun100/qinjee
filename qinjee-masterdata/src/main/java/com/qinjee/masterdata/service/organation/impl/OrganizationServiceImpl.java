package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.UserRoleService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 09:12:00
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private OrganizationHistoryService organizationHistoryService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public PageResult<Organization> getOrganizationTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();
        List<UserRole> userRoleList =  userRoleService.getUserRoleList(userSession.getArchiveId());
        Set<Integer> roleIds = userRoleList.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toSet());

        List<Organization> organizationList = organizationDao.getAllOrganization(archiveId, isEnable, roleIds);

        //获取第一级机构
        List<Organization> organizations = getFirstOrganizationList(organizationList);

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationList, organizations);

        return new PageResult<>(organizationList);
    }

    /**
     * 获取第一级机构
     * @param organizationList
     * @return
     */
    private List<Organization> getFirstOrganizationList(List<Organization> organizationList) {
        List<Organization> organizations = Collections.singletonList(organizationList.get(0));
        organizationList.removeAll(organizations);
        return organizations;
    }

    @Override
    public PageResult<Organization> getOrganizationList(OrganizationPageVo organizationPageVo, UserSession userSession) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(organizationPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        PageHelper.startPage(organizationPageVo.getCurrentPage(),organizationPageVo.getPageSize());
        List<Organization> organizationList = organizationDao.getOrganizationList(organizationPageVo,sortFieldStr,archiveId);
        PageResult<Organization> pageResult = new PageResult<>(organizationList);
        return pageResult;
    }

    @Override
    public PageResult<Organization> getOrganizationGraphics(UserSession userSession, Short isEnable, Integer orgId) {
        Integer archiveId = userSession.getArchiveId();
        Organization organization = organizationDao.selectByPrimaryKey(orgId);
        String orgCode = organization.getOrgCode() + "%";
        List<Organization> organizationList = organizationDao.getOrganizationGraphics(archiveId, isEnable, orgCode);
        PageResult<Organization> pageResult = new PageResult<>(organizationList);
        return pageResult;
    }

    @Transactional
    @Override
    public ResponseResult addOrganization(UserSession userSession, OrganizationVo organizationVo) {
        Organization organization = getNewOrgCode(organizationVo.getOrgParentId());
        String fullname = organization.getOrgFullname() + "/" + organizationVo.getOrgName();
        BeanUtils.copyProperties(organizationVo, organization);
        organization.setOrgFullname(fullname);
        organization.setOperatorId(userSession.getArchiveId());
        organization.setIsEnable((short) 1);
        int insert = organizationDao.insertSelective(organization);
        return insert == 1 ? new ResponseResult(CommonCode.SUCCESS) :  new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 获取新编码和sortId的机构
     * @param orgParentId
     * @return
     */
    private Organization getNewOrgCode(Integer orgParentId) {
        Organization parentOrganization = organizationDao.selectByPrimaryKey(orgParentId);
        List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(orgParentId);
        String parentOrgCode = parentOrganization.getOrgCode();
        String newOrgCode;
        Integer sortId;
        if(CollectionUtils.isEmpty(organizationList)){
            //新增第一个子级
            newOrgCode = parentOrgCode + "001";
            sortId = 1000;
        }else {
            //有子级
            Organization Lastorganization = organizationList.get(0);
            String orgCode = Lastorganization.getOrgCode();
            newOrgCode = getNewCode(orgCode);
            sortId = Lastorganization.getSortId() + 1000;
        }

        Organization organization = new Organization();
        organization.setSortId(sortId);
        organization.setOrgCode(newOrgCode);
        organization.setOrgFullname(parentOrganization.getOrgFullname());
        return organization;
    }

    /**
     * 获取新orgCode
     * @param orgCode
     * @return
     */
    private String getNewCode(String orgCode) {
        String number = orgCode.substring(orgCode.length() - 3);
        String preCode = orgCode.substring(0, orgCode.length() - 3);
        Integer new_OrgCode = Integer.parseInt(number) + 1;
        String code = new_OrgCode.toString();
        int i = 3 - code.length();
        if(i < 0){
            ExceptionCast.cast(CommonCode.ORGANIZATION_OUT_OF_RANGE);
        }
        for (int k = 0; k < i; k ++){
            code = "0" + code;
        }
        String newOrgCode = preCode + code;
        return newOrgCode;
    }

    @Transactional
    @Override
    public ResponseResult editOrganization(OrganizationVo organizationVo) {
        //通过机构id新增一条机构历史表
        Organization organization = addOrganizationHistoryByOrgId(organizationVo.getOrgId());
        BeanUtils.copyProperties(organizationVo, organization);
        Organization parentOrganization = organizationDao.selectByPrimaryKey(organization.getOrgParentId());
        String orgFullname = parentOrganization.getOrgFullname();
        organization.setOrgFullname(orgFullname + "/" + organization.getOrgName());
        int i = organizationDao.updateByPrimaryKeySelective(organization);

        //修改子级的机构全名称
        updateOrganizationFullName(organization);

        return i == 1 ? new ResponseResult(CommonCode.SUCCESS) :  new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 递归修改子级机构全名称
     * @param organization
     */
    private void updateOrganizationFullName(Organization organization) {
        List<Organization> organizationList = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
        if(!CollectionUtils.isEmpty(organizationList)){
            for (Organization organizati : organizationList) {
                OrganizationHistory organizationHistory = new OrganizationHistory();
                BeanUtils.copyProperties(organizati, organizationHistory);
                //新增机构历史信息
                organizationHistoryService.addOrganizationHistory(organizationHistory);
                organizati.setOrgFullname(organization.getOrgFullname() + "/" + organization.getOrgName());
                organizationDao.updateByPrimaryKeySelective(organizati);
                updateOrganizationFullName(organizati);
            }
        }
    }

    /**
     * 通过机构id新增一条机构历史表
     * @param orgId
     * @return
     */
    private Organization addOrganizationHistoryByOrgId(Integer orgId) {
        Organization organization = organizationDao.selectByPrimaryKey(orgId);
        OrganizationHistory organizationHistory = new OrganizationHistory();
        BeanUtils.copyProperties(organization, organizationHistory);
        //新增机构历史信息
        organizationHistoryService.addOrganizationHistory(organizationHistory);
        return organization;
    }

    @Transactional
    @Override
    public ResponseResult deleteOrganizationById(List<Integer> orgIds) {
        if(!CollectionUtils.isEmpty(orgIds)){
            for (Integer orgId : orgIds) {
                addOrganizationHistoryByOrgId(orgId);
                organizationDao.deleteByPrimaryKey(orgId);
            }
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult sealOrganizationByIds(List<Integer> orgIds, Short isEnable) {
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationDao.UpdateIsEnableByOrgIds(orgIds, isEnable);
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult mergeOrganization(String newOrgName, Integer targetOrgId, String orgType, List<Integer> orgIds, UserSession userSession) {
        List<Organization> organizationList = null;
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if(!CollectionUtils.isEmpty(organizationList)){
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if(OrgParentIds.size() != 1){
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }

            //获取新的合并机构
            Organization newOrganization = getNewOrganization(newOrgName, targetOrgId, orgType, userSession);
            organizationDao.insertSelective(newOrganization);

            updateOrganizationFullNameAndOrgCode(organizationList, newOrganization);
        }
        return new ResponseResult();
    }

    /**
     * 获取新的合并机构
     * @param newOrgName
     * @param targetOrgId
     * @param orgType
     * @param userSession
     * @return
     */
    private Organization getNewOrganization(String newOrgName, Integer targetOrgId, String orgType, UserSession userSession) {
        Organization newOrganization = new Organization();
        newOrganization.setOrgParentId(targetOrgId);
        newOrganization.setOrgName(newOrgName);
        Organization newOrgCode = getNewOrgCode(targetOrgId);

        String orgFullname = newOrgCode.getOrgFullname();
        BeanUtils.copyProperties(newOrgCode, newOrganization);
        newOrganization.setIsEnable((short) 1);
        newOrganization.setCompanyId(userSession.getCompanyId());
        newOrganization.setOrgType(orgType);
        newOrganization.setOrgFullname(orgFullname + "/" + newOrganization.getOrgName());
        return newOrganization;
    }

    @Override
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName) {
         //TODO 调用人员的接口
        return null;
    }

    @Override
    public ResponseResult sortOrganization(Integer preOrgId, Integer midOrgId, Integer nextOrgId) {
        Organization preOrganization;
        Organization nextOrganization;
        Integer midSort = null;
        if(nextOrgId != null){
            //移动
            nextOrganization = organizationDao.selectByPrimaryKey(nextOrgId);
            midSort = nextOrganization.getSortId() - 1;

        }else if(nextOrgId == null){
            //移动到最后
            preOrganization = organizationDao.selectByPrimaryKey(preOrgId);
            midSort = preOrganization.getSortId() + 1;
        }
        Organization organization = new Organization();
        organization.setOrgId(midOrgId);
        organization.setSortId(midSort);
        organizationDao.insertSelective(organization);
        return new ResponseResult();
    }

    @Override
    public ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId) {

        List<Organization> organizationList = null;
        if(!CollectionUtils.isEmpty(orgIds)){
            organizationList = organizationDao.getOrganizationListByOrgIds(orgIds);
        }

        //判断是否是同一个父级下的
        if(!CollectionUtils.isEmpty(organizationList)){
            Set<Integer> OrgParentIds = organizationList.stream().map(organization -> organization.getOrgParentId()).collect(Collectors.toSet());
            if(OrgParentIds.size() != 1){
                //不是
                return new ResponseResult(CommonCode.FAIL);
            }
            Organization parentOrganization = organizationDao.selectByPrimaryKey(targetOrgId);

            updateOrganizationFullNameAndOrgCode(organizationList, parentOrganization);
        }

        return new ResponseResult();
    }

    /**
     * 修改本级的父级id和全名称及子级的全名称
     * @param organizationList
     * @param parentOrganization
     */
    private void updateOrganizationFullNameAndOrgCode(List<Organization> organizationList, Organization parentOrganization) {
        String newOrgCode = parentOrganization.getOrgCode() + "000";
        for (Organization organization : organizationList) {
            OrganizationHistory organizationHistory = new OrganizationHistory();
            BeanUtils.copyProperties(organization, organizationHistory);
            organizationHistoryService.addOrganizationHistory(organizationHistory);
            organization.setOrgParentId(parentOrganization.getOrgId());
            organization.setOrgFullname(parentOrganization.getOrgFullname() + "/" + organization.getOrgName());
            newOrgCode = getNewCode(newOrgCode);
            organization.setOrgCode(newOrgCode);
            organizationDao.updateByPrimaryKeySelective(organization);

            List<Organization> organizations = organizationDao.getOrganizationListByParentOrgId(organization.getOrgId());
            if(!CollectionUtils.isEmpty(organizations)){
                //递归修改本级的父级id和全名称及子级的全名称
                updateOrganizationFullNameAndOrgCode(organizations, organization);
            }

        }
    }

    /**
     * 处理所有机构以树形结构展示
     * @param organizationList
     * @param organizations
     */
    private void handlerOrganizationToTree(List<Organization> organizationList, List<Organization> organizations) {
        for (Organization org : organizations) {
            Integer orgId = org.getOrgId();
            List<Organization> childList = organizationList.stream().filter(organization -> {
                Integer orgParentId = organization.getOrgParentId();
                if (orgParentId != null && orgParentId > 0) {
                    return orgParentId == orgId;
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if(childList != null && childList.size() > 0){
                org.setChildList(childList);
                organizationList.removeAll(childList);
                handlerOrganizationToTree(organizationList,childList);
            }
        }
    }
}
