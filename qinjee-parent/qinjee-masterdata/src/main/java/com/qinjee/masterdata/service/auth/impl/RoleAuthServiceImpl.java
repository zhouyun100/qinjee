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
import com.qinjee.masterdata.model.vo.auth.*;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<RoleGroupVO> searchRoleTree(Integer companyId) {
        if(companyId == null){
            return null;
        }

        List<RoleGroupVO> roleGroupList = roleAuthDao.searchRoleTree(companyId);

        /**
         * 如果角色列表为空则直接返回null
         */
        if(CollectionUtils.isEmpty(roleGroupList)){
            return null;
        }

        /**
         * 提取当前角色树的一级节点
         */
        List<RoleGroupVO> firstRoleList = roleGroupList.stream().filter(roleGroupVO -> {
            if(roleGroupVO.getParentRoleGroupId() == null || roleGroupVO.getParentRoleGroupId() == 0){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        handlerRoleToTree(roleGroupList,firstRoleList);

        return firstRoleList;
    }

    @Override
    public List<MenuVO> searchRoleAuthTree(Integer operatorId, Integer roleId, Integer companyId) {
        if(roleId == null || companyId == null){
            return null;
        }

        RequestRoleVO requestRole = new RequestRoleVO();
        requestRole.setRoleId(roleId);
        requestRole.setCompanyId(companyId);
        requestRole.setOperatorId(operatorId);
        requestRole.setCurrentDateTime(new Date());
        List<MenuVO> menuList= roleAuthDao.searchRoleAuthTree(requestRole);

        /**
         * 如果菜单列表为空则直接返回null
         */
        if(CollectionUtils.isEmpty(menuList)){
            return null;
        }

        /**
         * 提取当前菜单树的一级节点
         */
        List<MenuVO> firstLevelMenuList = menuList.stream().filter(menu -> {
            if(menu.getParentMenuId() == 0){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        /**
         * 处理所有菜单列表以树形结构展示
         */
        handlerMenuToTree(menuList,firstLevelMenuList);

        return firstLevelMenuList;
    }

    @Override
    public List<OrganizationVO> searchOrgAuthTree(Integer operatorId, Integer roleId, Integer archiveId) {
        if(roleId == null || operatorId == null){
            return null;
        }

        RequestRoleVO requestRole = new RequestRoleVO();
        requestRole.setOperatorId(operatorId);
        requestRole.setRoleId(roleId);
        requestRole.setArchiveId(archiveId);
        requestRole.setCurrentDateTime(new Date());
        List<OrganizationVO> organizationList = roleAuthDao.searchOrgAuthTree(requestRole);
        /**
         * 如果机构列表为空则直接返回null
         */
        if(CollectionUtils.isEmpty(organizationList)){
            return null;
        }

        /**
         * 提取当前机构树的一级节点
         */
        List<OrganizationVO> firstLevelMenuList = organizationList.stream().filter(organization -> {
            if(organization.getOrgParentId() == 0){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        /**
         * 处理所有机构列表以树形结构展示
         */
        handlerOrgToTree(organizationList, firstLevelMenuList);

        return firstLevelMenuList;
    }

    @Override
    public int addRoleGroup(String roleGroupName, Integer companyId, Integer operatorId) {

        if(StringUtils.isEmpty(roleGroupName) || null == companyId || null == operatorId){
            return 0;
        }
        int resultNumber;
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setRoleGroupName(roleGroupName);
        roleGroup.setCompanyId(companyId);
        roleGroup.setOperatorId(operatorId);
        resultNumber = roleAuthDao.addRoleGroup(roleGroup);
        return resultNumber;
    }

    @Override
    public int addRole(Integer roleGroupId, String roleName, Integer companyId, Integer operatorId) {
        if(StringUtils.isEmpty(roleName) || null == roleGroupId || null == companyId || null == operatorId){
            return 0;
        }
        int resultNumber;
        Role role = new Role();
        role.setRoleGroupId(roleGroupId);
        role.setRoleName(roleName);
        role.setCompanyId(companyId);
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
    public Role searchRoleDetailByRoleId(Integer roleId) {
        if(null == roleId){
            return null;
        }
        Role role = roleAuthDao.searchRoleDetailByRoleId(roleId);
        return role;
    }

    @Override
    public int updateRoleAutoAuthChildOrgByRoleId(Integer roleId, Integer isAutoAuthChildOrg, Integer operatorId) {
        if(null == roleId || null == isAutoAuthChildOrg || null == operatorId){
            return 0;
        }
        int resultNumber;
        Role role = new Role();
        role.setRoleId(roleId);
        role.setIsAutoAuthChildOrg(isAutoAuthChildOrg);
        role.setOperatorId(operatorId);
        resultNumber = roleAuthDao.updateRoleAutoAuthChildOrgByRoleId(role);
        return resultNumber;
    }

    @Override
    public int updateRoleGroup(Integer roleGroupId, String roleGroupName, Integer operatorId) {
        if(null == roleGroupId || StringUtils.isEmpty(roleGroupName) || null == operatorId){
            return 0;
        }
        int resultNumber;
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setRoleGroupId(roleGroupId);
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

    @Transactional(rollbackFor = Exception.class)
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

        if(CollectionUtils.isNotEmpty(menuIdList)){
            for(Integer menuId : menuIdList){
                if(CollectionUtils.isNotEmpty(menuList)){
                    for(MenuVO menu : menuList){
                        if(menuId.equals(menu.getMenuId())){
                            tempMenuIdList.add(menuId);
                            tempMenuList.add(menu);
                        }
                    }
                }
            }

            menuIdList.removeAll(tempMenuIdList);
            for(Integer menuId : menuIdList){
                roleMenuAuth.setMenuId(menuId);
                rowNumber = roleAuthDao.addRoleMenuAuth(roleMenuAuth);
                resultNumber += rowNumber;
            }
        }

        if(CollectionUtils.isNotEmpty(menuList)){
            menuList.removeAll(tempMenuList);
            for(MenuVO menu : menuList){
                roleMenuAuth.setMenuId(menu.getMenuId());
                rowNumber = roleAuthDao.delRoleMenuAuth(roleMenuAuth);
                resultNumber += rowNumber;
            }
        }
        return resultNumber;
    }

    @Transactional(rollbackFor = Exception.class)
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

        if(CollectionUtils.isNotEmpty(orgIdList)){
            if(CollectionUtils.isNotEmpty(organizationList)){
                for(Integer orgId : orgIdList){
                    for(OrganizationVO organization : organizationList){
                        if(orgId.equals(organization.getOrgId())){
                            tempOrgIdList.add(orgId);
                            tempOrganizationList.add(organization);
                        }
                    }
                }
            }
            orgIdList.removeAll(tempOrgIdList);
            for(Integer orgId : orgIdList){
                roleOrgAuth.setOrgId(orgId);
                rowNumber = roleAuthDao.addRoleOrgAuth(roleOrgAuth);
                resultNumber += rowNumber;
            }
        }

        if(CollectionUtils.isNotEmpty(organizationList)){
            organizationList.removeAll(tempOrganizationList);
            for(OrganizationVO organization : organizationList){
                roleOrgAuth.setOrgId(organization.getOrgId());
                rowNumber = roleAuthDao.delRoleOrgAuth(roleOrgAuth);
                resultNumber += rowNumber;
            }
        }
        return resultNumber;
    }

    @Override
    public List<CustomArchiveTableFieldVO> searchCustomArchiveTableList(Integer companyId) {
        List<CustomArchiveTableFieldVO> customArchiveTableList = roleAuthDao.searchCustomArchiveTableListByCompanyId(companyId);
        return customArchiveTableList;
    }

    @Override
    public List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByTableId(Integer roleId, Integer tableId) {
        CustomArchiveTableFieldVO requestCustomTableField = new CustomArchiveTableFieldVO();
        requestCustomTableField.setRoleId(roleId);
        requestCustomTableField.setTableId(tableId);
        List<CustomArchiveTableFieldVO> customArchiveFieldList = roleAuthDao.searchCustomArchiveTableFieldListByTableId(requestCustomTableField);
        return customArchiveFieldList;
    }

    @Override
    public List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByRoleId(Integer roleId) {
        List<CustomArchiveTableFieldVO> customArchiveTableList = roleAuthDao.searchCustomArchiveTableFieldListByRoleIdAndTableId(roleId ,null);
        return customArchiveTableList;
    }

    @Override
    public void handlerRoleToTree(List<RoleGroupVO> allRoleGroupList, List<RoleGroupVO> firstLevelRoleList) {
        for (RoleGroupVO roleGroupVO : firstLevelRoleList) {
            if(roleGroupVO.getRoleType().equals("ROLE_GROUP")){
                List<RoleGroupVO> childList = allRoleGroupList.stream().filter(role -> {
                    if (role.getRoleType().equals("ROLE") && role.getParentRoleGroupId() != null && role.getParentRoleGroupId().equals(roleGroupVO.getRoleGroupId())) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(childList)){
                    allRoleGroupList.removeAll(childList);
                    roleGroupVO.setChildRoleGroupList(childList);
                }else{
                    roleGroupVO.setChildRoleGroupList(new ArrayList<>());
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateRoleCustomArchiveTableFieldAuth(List<CustomArchiveTableFieldVO> requestCustomTableFieldList, Integer operatorId) {
        int resultNumber = 0;
        if(CollectionUtils.isNotEmpty(requestCustomTableFieldList)){

            //定义已存在的字段权限列表
            List<CustomArchiveTableFieldVO> customArchiveTableList = null;

            //定义更新前后同时存在的字段权限列表
            List<CustomArchiveTableFieldVO> tempCustomArchiveTableList = new ArrayList<>();
            List<CustomArchiveTableFieldVO> tempRequestCustomTableFieldList = new ArrayList<>();

            CustomArchiveTableFieldVO customArchiveTableFieldVO = requestCustomTableFieldList.get(0);
            //获取角色ID
            Integer roleId = customArchiveTableFieldVO.getRoleId();
            Integer tableId = customArchiveTableFieldVO.getTableId();

            //查询角色自定义表已有字段权限
            if(roleId != null && tableId != null){
                customArchiveTableList = roleAuthDao.searchCustomArchiveTableFieldListByRoleIdAndTableId(roleId, tableId);
            }

            //筛选
            if(CollectionUtils.isNotEmpty(customArchiveTableList)){
                for(CustomArchiveTableFieldVO requestCustomTableField : requestCustomTableFieldList){
                    for(CustomArchiveTableFieldVO customTableField : customArchiveTableList){
                        if(requestCustomTableField.getFieldId().equals(customTableField.getFieldId())
                                && requestCustomTableField.getRoleId().equals(customTableField.getRoleId())){
                            tempCustomArchiveTableList.add(customTableField);
                            tempRequestCustomTableFieldList.add(requestCustomTableField);
                        }
                    }
                }
                customArchiveTableList.removeAll(tempCustomArchiveTableList);
                requestCustomTableFieldList.removeAll(tempRequestCustomTableFieldList);
            }

            //删除
            for(CustomArchiveTableFieldVO customTableField : customArchiveTableList){
                customTableField.setOperatorId(operatorId);
                customTableField.setIsDelete(1);
                resultNumber = roleAuthDao.updateRoleCustomArchiveTableFieldAuth(customTableField);
            }

            //修改
            for(CustomArchiveTableFieldVO tempRequestCustomTableField : tempRequestCustomTableFieldList){
                tempRequestCustomTableField.setOperatorId(operatorId);
                tempRequestCustomTableField.setIsDelete(0);
                resultNumber = roleAuthDao.updateRoleCustomArchiveTableFieldAuth(tempRequestCustomTableField);
            }

            //新增
            for(CustomArchiveTableFieldVO requestCustomTableField : requestCustomTableFieldList){
                requestCustomTableField.setOperatorId(operatorId);
                requestCustomTableField.setIsDelete(0);
                resultNumber = roleAuthDao.addRoleCustomArchiveTableFieldAuth(requestCustomTableField);
            }
        }
        return resultNumber;
    }

    @Override
    public void handlerMenuToTree(List<MenuVO> allMenuVOList, List<MenuVO> firstLevelMenuList) {
        for (MenuVO menuVO : firstLevelMenuList) {
            List<MenuVO> childList = allMenuVOList.stream().filter(menu -> {
                if (menuVO.getMenuId().equals(menu.getParentMenuId())) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            /**
             * 判断是否还有子级，如果有则递归处理
             */
            if(CollectionUtils.isNotEmpty(childList)){
                menuVO.setChildMenuList(childList);
                allMenuVOList.removeAll(childList);
                handlerMenuToTree(allMenuVOList,childList);
            }else{
                menuVO.setChildMenuList(new ArrayList<>());
            }
        }
    }

    @Override
    public void handlerOrgToTree(List<OrganizationVO> allOrgList, List<OrganizationVO> firstLevelOrgList) {
        for (OrganizationVO organizationVO : firstLevelOrgList) {
            List<OrganizationVO> childList = allOrgList.stream().filter(organization -> {
                if (organizationVO.getOrgId().equals(organization.getOrgParentId())) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            /**
             * 判断是否还有子级，如果有则递归处理
             */
            if(CollectionUtils.isNotEmpty(childList)){
                organizationVO.setChildOrganizationList(childList);
                allOrgList.removeAll(childList);
                handlerOrgToTree(allOrgList,childList);
            }else{
                organizationVO.setChildOrganizationList(new ArrayList<>());
            }
        }
    }

    @Override
    public int saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO) {
//        return roleAuthDao.saveRoleDataLevelAuth(roleDataLevelAuthVO);
        return 0;
    }
}
