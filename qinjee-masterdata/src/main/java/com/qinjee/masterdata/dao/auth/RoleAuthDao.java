/**
 * 文件名：RoleAuthDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/23
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.auth;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.auth.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色授权
 * @author 周赟
 * @date 2019/9/23
 */
@Service
public interface RoleAuthDao {

    /**
     * 根据企业ID查询角色树
     * @param companyId
     * @return
     */
    List<RoleGroupVO> searchRoleTree(Integer companyId);

    /**
     * 根据角色ID查询角色功能权限树
     * @param requestRole
     * @return
     */
    List<MenuVO> searchRoleAuthTree(RequestRoleVO requestRole);

    /**
     * 查询角色机构权限树
     * @param requestRole
     * @return
     */
    List<OrganizationVO> searchOrgAuthTree(RequestRoleVO requestRole);

    /**
     * 新增角色组
     * @param roleGroup
     * @return
     */
    int addRoleGroup(RoleGroup roleGroup);

    /**
     * 新增角色
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 修改角色
     * @param role
     * @return
     */
    int updateRole(Role role);

    /**
     * 修改角色组
     * @param roleGroup
     * @return
     */
    int updateRoleGroup(RoleGroup roleGroup);

    /**
     * 根据角色ID删除角色
     * @param role
     * @return
     */
    int delRole(Role role);

    /**
     * 根据角色组ID删除角色组
     * @param roleGroup
     * @return
     */
    int delRoleGroup(RoleGroup roleGroup);


    /**
     * 根据角色ID查询已有的菜单列表
     * @param roleId
     * @return
     */
    List<MenuVO> searchRoleMenuListByRoleId(Integer roleId);

    /**
     * 新增角色功能权限
     * @param roleMenuAuth
     * @return
     */
    int addRoleMenuAuth(RoleMenuAuth roleMenuAuth);

    /**
     * 删除角色功能权限
     * @param roleMenuAuth
     * @return
     */
    int delRoleMenuAuth(RoleMenuAuth roleMenuAuth);


    /**
     * 根据角色ID查询已有的机构列表
     * @param roleId
     * @return
     */
    List<OrganizationVO> searchRoleOrgListByRoleId(Integer roleId);

    /**
     * 新增角色机构权限
     * @param roleOrgAuth
     * @return
     */
    int addRoleOrgAuth(RoleOrgAuth roleOrgAuth);

    /**
     * 删除角色机构权限
     * @param roleOrgAuth
     * @return
     */
    int delRoleOrgAuth(RoleOrgAuth roleOrgAuth);

    /**
     * 根据企业ID查询企业自定义表列表
     * @param companyId
     * @return
     */
    List<CustomArchiveTableFieldVO> searchCustomArchiveTableListByCompanyId(Integer companyId);

    /**
     * 根据自定义表ID查询自定义字段列表
     * @param requestCustomTableField
     * @return
     */
    List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByTableId(RequestCustomTableFieldVO requestCustomTableField);

    /**
     * 根据角色ID查询角色自定义表字段列表
     * @param roleId
     * @return
     */
    List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByRoleId(Integer roleId);

    /**
     * 查询角色自定义人员表字段
     * @param requestCustomTableField
     * @return
     */
    RequestCustomTableFieldVO searchRoleCustomArchiveTableFieldAuth(RequestCustomTableFieldVO requestCustomTableField);

    /**
     * 新增角色自定义人员表字段权限
     * @param requestCustomTableField
     * @return
     */
    int addRoleCustomArchiveTableFieldAuth(RequestCustomTableFieldVO requestCustomTableField);

    /**
     * 修改角色自定义人员表字段权限
     * @param requestCustomTableField
     * @return
     */
    int updateRoleCustomArchiveTableFieldAuth(RequestCustomTableFieldVO requestCustomTableField);

    /**
     * 保存数据级权限定义
     * @param roleDataLevelAuthVO
     * @return
     */
    int saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO);
}
