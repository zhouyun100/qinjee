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

import com.qinjee.masterdata.model.entity.Role;
import com.qinjee.masterdata.model.entity.RoleGroup;
import com.qinjee.masterdata.model.entity.RoleMenuAuth;
import com.qinjee.masterdata.model.entity.RoleOrgAuth;
import com.qinjee.masterdata.model.vo.auth.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色授权
 * @author 周赟
 * @date 2019/9/23
 */
@Repository
public interface RoleAuthDao {

    /**
     * 根据企业ID和档案ID查询角色树
     * @param companyId
     * @param archiveId
     * @return
     */
    List<RoleGroupVO> searchRoleTree(Integer companyId,Integer archiveId);

    /**
     * 根据企业ID、档案ID、角色ID查询角色树
     * @param companyId
     * @param archiveId
     * @param roleId
     * @return
     */
    List<RoleGroupVO> searchRoleTreeByRoleId(Integer companyId,Integer archiveId,Integer roleId);

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
     * 根据角色ID查询角色详情
     * @param roleId
     * @return
     */
    Role searchRoleDetailByRoleId(Integer roleId);

    /**
     * 根据角色ID修改角色是否自动授权新增子集机构权限
     * @param role
     * @return
     */
    int updateRoleAutoAuthChildOrgByRoleId(Role role);

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
     * 根据角色ID查询授权的子集角色ID集合
     * @param roleId
     * @return
     */
    List<Integer> searchChildRoleIdListByRoleId(Integer roleId);

    /**
     * 新增角色授权角色
     * @param parentRoleId
     * @param childRoleIdList
     * @param operatorId
     * @return
     */
    int addRoleRoleRelation(Integer parentRoleId,List<Integer> childRoleIdList,Integer operatorId);

    /**
     * 根据角色ID删除子集角色授权
     * @param parentRoleId
     * @param childRoleIdList
     * @param operatorId
     * @return
     */
    int deleteRoleRoleRelation(Integer parentRoleId,List<Integer> childRoleIdList,Integer operatorId);


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
    List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByTableId(CustomArchiveTableFieldVO requestCustomTableField);

    /**
     * 根据角色ID查询角色自定义表字段列表
     * @param roleId
     * @return
     */
    List<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByRoleIdAndTableId(Integer roleId, Integer tableId);

    /**
     * 查询角色自定义人员表字段
     * @param requestCustomTableField
     * @return
     */
    CustomArchiveTableFieldVO searchRoleCustomArchiveTableFieldAuth(CustomArchiveTableFieldVO requestCustomTableField);

    /**
     * 新增角色自定义人员表字段权限
     * @param requestCustomTableField
     * @return
     */
    int addRoleCustomArchiveTableFieldAuth(CustomArchiveTableFieldVO requestCustomTableField);

    /**
     * 修改角色自定义人员表字段权限
     * @param requestCustomTableField
     * @return
     */
    int updateRoleCustomArchiveTableFieldAuth(CustomArchiveTableFieldVO requestCustomTableField);

    /**
     * 保存数据级权限定义
     * @param roleDataLevelAuthVO
     * @return
     */
//    int saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO);
}
