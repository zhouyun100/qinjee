package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

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

    /**
     * 编辑机构
     * @param organizationVo
     * @return
     */
    ResponseResult editOrganization(OrganizationVo organizationVo);

    /**
     * 删除机构
     * @param orgIds
     * @return
     */
    ResponseResult deleteOrganizationById(List<Integer> orgIds);

    /**
     * 封存/封存机构
     * @param orgIds
     * @param isEnable
     * @return
     */
    ResponseResult sealOrganizationByIds(List<Integer> orgIds, Short isEnable);

    /**
     * 合并机构
     * @param newOrgName
     * @param targetOrgId
     * @param orgIds
     * @return
     */
    ResponseResult mergeOrganization(String newOrgName, Integer targetOrgId, String orgType, List<Integer> orgIds, UserSession userSession);

    /**
     * 机构负责人查询
     * @param userName
     * @return
     */
    ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(String userName);

    /**
     * 机构排序
     * @param preOrgId
     * @param midOrgId
     * @param nextOrgId
     * @return
     */
    ResponseResult sortOrganization(Integer preOrgId, Integer midOrgId, Integer nextOrgId);

    /**
     * 划转机构
     * @param orgIds
     * @param targetOrgId
     * @return
     */
    ResponseResult transferOrganization(List<Integer> orgIds, Integer targetOrgId);

    /**
     * 机构职位树状图展示
     * @param userSession
     * @return
     */
    ResponseResult<List<Organization>> getOrganizationPositionTree(UserSession userSession, Short isEnable);
}
