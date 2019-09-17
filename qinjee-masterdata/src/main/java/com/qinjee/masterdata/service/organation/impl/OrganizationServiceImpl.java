package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.OrganizationDao;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public PageResult<Organization> getOrganizationTree(UserSession userSession, Short isEnable) {
        Integer archiveId = userSession.getArchiveId();
        Integer companyId = userSession.getCompanyId();
        List<Organization> organizationList = organizationDao.getAllOrganization(archiveId, isEnable);

        //获取第一级机构
        List<Organization> organizations = organizationList.stream().filter(organization -> {
            Integer orgParentId = organization.getOrgParentId();
            if (orgParentId != null && orgParentId > 0) {
                return companyId == 0;
            }
            return false;
        }).collect(Collectors.toList());

        organizationList.removeAll(organizations);

        //递归处理机构,使其以树形结构展示
        handlerOrganizationToTree(organizationList, organizations);

        return new PageResult<>(organizationList);
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

        
        //TODO  sql完善
        List<Organization> organizationList = organizationDao.getOrganizationGraphics(archiveId, isEnable, orgId);
        PageResult<Organization> pageResult = new PageResult<>(organizationList);
        return pageResult;
    }

    @Override
    public ResponseResult addOrganization(UserSession userSession, OrganizationVo organizationVo) {

        return null;
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
