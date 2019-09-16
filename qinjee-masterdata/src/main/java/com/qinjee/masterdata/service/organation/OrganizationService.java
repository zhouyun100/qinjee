package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 09:11:00
 */
public interface OrganizationService {

    /**
     * 根据是否封存查询用户下所有的机构,树形结构展示
     * @param userSession
     * @param isEnable
     * @return
     */
    PageResult<Organization> getOrganizationTree(UserSession userSession, Short isEnable);

    /**
     * 根据条件分页查询用户下所有的机构
     * @param organizationPageVo
     * @return
     */
    PageResult<Organization> getOrganizationList(OrganizationPageVo organizationPageVo, UserSession userSession);

    /**
     * 根据是否封存查询用户下所有的机构,图形化展示
     * @param userSession
     * @param isEnable
     * @return
     */
    PageResult<Organization> getOrganizationGraphics(UserSession userSession, Short isEnable, Integer orgId);

    /**
     * 新增机构
     * @param userSession
     * @param organizationVo
     * @return
     */
    ResponseResult addOrganization(UserSession userSession, OrganizationVo organizationVo);
}
