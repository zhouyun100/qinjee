/**
 * 文件名：RoleAuthServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/23
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.qinjee.masterdata.dao.auth.RoleAuthDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.model.vo.auth.RoleDataLevelAuthVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色授权实现类
 * @author 周赟
 * @date 2019/9/23
 */
@Service
public class RoleAuthServiceImpl implements RoleAuthService {

    @Autowired
    private RoleAuthDao roleAuthDao;

    @Override
    // TODO 角色树
    public List<RoleGroupVO> searchRoleTree(Integer companyId) {
        List<RoleGroupVO> roleGroupList = roleAuthDao.searchRoleTree(companyId);
        return roleGroupList;
    }

    @Override
    // TODO 菜单树
    public List<MenuVO> searchRoleAuthTree(Integer roleId, Integer companyId) {
        List<MenuVO> menuList= roleAuthDao.searchRoleAuthTree(roleId,companyId);
        return menuList;
    }

    @Override
    // TODO 机构树
    public List<OrganizationVO> searchOrgAuthTree(Integer roleId, Integer companyId) {
        List<OrganizationVO> organizationList = roleAuthDao.searchOrgAuthTree(roleId, companyId);
        return organizationList;
    }

    @Override
    public int addRoleGroup(Integer parentRoleGroupId, String roleGroupName, Integer companyId, Integer operatorId) {

        if(null == parentRoleGroupId || StringUtils.isEmpty(roleGroupName) || null == companyId || null == operatorId){
            return 0;
        }
        int resultNumber;
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setParentRoleGroupId(parentRoleGroupId);
        roleGroup.setRoleGroupName(roleGroupName);
        roleGroup.setCompanyId(companyId);
        roleGroup.setOperatorId(operatorId);
        resultNumber = roleAuthDao.addRoleGroup(roleGroup);
        return resultNumber;
    }

    @Override
    public int addRole(Integer roleGroupId, String roleName, Integer operatorId) {
        if(null == roleGroupId || StringUtils.isEmpty(roleName) || null == operatorId){
            return 0;
        }
        int resultNumber;
        Role role = new Role();
        role.setRoleGroupId(roleGroupId);
        role.setRoleName(roleName);
        role.setOperatorId(operatorId);
        resultNumber = roleAuthDao.addRole(role);
        return resultNumber;
    }

    @Override
    public int updateRole(Integer roleId, Integer roleGroupId, String roleName, Integer operatorId) {
        if(null == roleGroupId || StringUtils.isEmpty(roleName) || null == operatorId){
            return 0;
        }
        int resultNumber;
        Role role = new Role();
        role.setRoleId(roleId);
        role.setRoleGroupId(roleGroupId);
        role.setRoleName(roleName);
        role.setOperatorId(operatorId);
        resultNumber = roleAuthDao.updateRole(role);
        return resultNumber;
    }

    @Override
    public int updateRoleGroup(Integer roleGroupId, Integer parentRoleGroupId, String roleGroupName, Integer operatorId) {
        if(null == roleGroupId || null == parentRoleGroupId || StringUtils.isEmpty(roleGroupName) || null == operatorId){
            return 0;
        }
        int resultNumber;
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setRoleGroupId(roleGroupId);
        roleGroup.setParentRoleGroupId(parentRoleGroupId);
        roleGroup.setRoleGroupName(roleGroupName);
        roleGroup.setOperatorId(operatorId);
        resultNumber = roleAuthDao.updateRoleGroup(roleGroup);
        return resultNumber;
    }

    @Override
    public int delRole(Integer roleId, Integer operatorId) {
        if(null == roleId || null == operatorId){
            return 0;
        }
        int resultNumber;
        Role role = new Role();
        role.setRoleId(roleId);
        role.setOperatorId(operatorId);
        resultNumber = roleAuthDao.delRole(role);
        return resultNumber;
    }

    @Override
    public int delRoleGroup(Integer roleGroupId, Integer operatorId) {
        if(null == roleGroupId || null == operatorId){
            return 0;
        }
        int resultNumber;
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setRoleGroupId(roleGroupId);
        roleGroup.setOperatorId(operatorId);
        resultNumber = roleAuthDao.delRoleGroup(roleGroup);
        return resultNumber;
    }

    @Override
    public int updateRoleMenuAuth(Integer roleId, List<Integer> menuIdList, Integer operatorId) {
        if(null == roleId){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        List<Integer> tempMenuIdList = new ArrayList<>();
        List<MenuVO> tempMenuList = new ArrayList<>();

        RoleMenuAuth roleMenuAuth = new RoleMenuAuth();
        roleMenuAuth.setRoleId(roleId);
        roleMenuAuth.setOperatorId(operatorId);

        List<MenuVO> menuList = roleAuthDao.searchRoleMenuListByRoleId(roleId);

        if(!CollectionUtils.isEmpty(menuIdList)){
            for(Integer menuId : menuIdList){
                if(!CollectionUtils.isEmpty(menuList)){
                    for(MenuVO menu : menuList){
                        if(menuId.equals(menu.getMenuId())){
                            tempMenuIdList.add(menuId);
                            tempMenuList.add(menu);
                        }
                    }
                }
            }
            menuIdList.remove(tempMenuIdList);
            for(Integer menuId : menuIdList){
                roleMenuAuth.setMenuId(menuId);
                rowNumber = roleAuthDao.addRoleMenuAuth(roleMenuAuth);
                resultNumber += rowNumber;
            }
        }

        if(!CollectionUtils.isEmpty(menuList)){
            menuList.remove(tempMenuList);
            for(MenuVO menu : menuList){
                roleMenuAuth.setMenuId(menu.getMenuId());
                rowNumber = roleAuthDao.delRoleMenuAuth(roleMenuAuth);
                resultNumber += rowNumber;
            }
        }
        return resultNumber;
    }

    @Override
    public int updateRoleOrgAuth(Integer roleId, List<Integer> orgIdList, Integer operatorId) {

        int resultNumber = 0;
        int rowNumber;
        List<Integer> tempOrgIdList = new ArrayList<>();
        List<OrganizationVO> tempOrganizationList = new ArrayList<>();

        RoleOrgAuth roleOrgAuth = new RoleOrgAuth();
        roleOrgAuth.setRoleId(roleId);
        roleOrgAuth.setOperatorId(operatorId);

        List<OrganizationVO> organizationList = roleAuthDao.searchRoleOrgListByRoleId(roleId);

        if(!CollectionUtils.isEmpty(orgIdList)){
            for(Integer orgId : orgIdList){
                if(!CollectionUtils.isEmpty(organizationList)){
                    for(OrganizationVO organization : organizationList){
                        if(orgId.equals(organization.getOrgId())){
                            tempOrgIdList.add(orgId);
                            tempOrganizationList.add(organization);
                        }
                    }
                }
            }
            orgIdList.remove(tempOrgIdList);
            for(Integer orgId : orgIdList){
                roleOrgAuth.setOrgId(orgId);
                rowNumber = roleAuthDao.addRoleOrgAuth(roleOrgAuth);
                resultNumber += rowNumber;
            }
        }

        if(!CollectionUtils.isEmpty(organizationList)){
            organizationList.remove(tempOrganizationList);
            for(OrganizationVO organization : organizationList){
                roleOrgAuth.setOrgId(organization.getOrgId());
                rowNumber = roleAuthDao.delRoleOrgAuth(roleOrgAuth);
                resultNumber += rowNumber;
            }
        }
        return resultNumber;
    }

    @Override
    public List<CustomArchiveTable> searchCustomArchiveTableList(Integer roleId, Integer companyId) {
        return null;
    }

    @Override
    public List<CustomArchiveField> searchCustomArchiveTableFieldListByTableId(Integer tableId) {
        return null;
    }

    @Override
    public List<CustomArchiveField> searchCustomArchiveTableFieldListByRoleId(Integer roleId, Integer companyId) {
        return null;
    }

    @Override
    public int updateRoleCustomArchiveTableFieldAuth(Integer roleId, List<Integer> fieldIdList, Integer operatorId) {
        return 0;
    }

    @Override
    public int saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO) {
        return 0;
    }
}
