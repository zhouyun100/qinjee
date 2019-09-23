/**
 * 文件名：RoleAuthService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/23
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth;


import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.model.vo.auth.RoleDataLevelAuthVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;

import java.util.List;

/**
 * 角色授权
 * @author 周赟
 * @date 2019/9/23
 */
public interface RoleAuthService {

    /**
     * 根据企业ID查询角色树
     * @param companyId
     * @return
     */
    List<RoleGroupVO> searchRoleTree(Integer companyId);

    /**
     * 根据角色ID查询角色功能权限树
     * @param roleId
     * @param companyId
     * @return
     */
    List<MenuVO> searchRoleAuthTree(Integer roleId, Integer companyId);

    /**
     * 查询角色机构权限树
     * @param roleId
     * @param companyId
     * @return
     */
    List<OrganizationVO> searchOrgAuthTree(Integer roleId, Integer companyId);

    /**
     * 新增角色组
     * @param parentRoleGroupId
     * @param roleGroupName
     * @param companyId
     * @param operatorId
     * @return
     */
    int addRoleGroup(Integer parentRoleGroupId, String roleGroupName, Integer companyId, Integer operatorId);

    /**
     * 新增角色
     * @param roleGroupId
     * @param roleName
     * @param operatorId
     * @return
     */
    int addRole(Integer roleGroupId, String roleName, Integer operatorId);

    /**
     * 修改角色
     * @param roleId
     * @param roleGroupId
     * @param roleName
     * @param operatorId
     * @return
     */
    int updateRole(Integer roleId, Integer roleGroupId, String roleName, Integer operatorId);

    /**
     * 修改角色组
     * @param roleGroupId
     * @param parentRoleGroupId
     * @param roleGroupName
     * @param operatorId
     * @return
     */
    int updateRoleGroup(Integer roleGroupId, Integer parentRoleGroupId, String roleGroupName, Integer operatorId);

    /**
     * 根据角色ID删除角色
     * @param roleId
     * @param operatorId
     * @return
     */
    int delRole(Integer roleId, Integer operatorId);

    /**
     * 根据角色ID删除角色组
     * @param roleGroupId
     * @param operatorId
     * @return
     */
    int delRoleGroup(Integer roleGroupId, Integer operatorId);

    /**
     * 修改角色功能权限
     * @param roleId
     * @param menuIdList
     * @param operatorId
     * @return
     */
    int updateRoleMenuAuth(Integer roleId, List<Integer> menuIdList, Integer operatorId);

    /**
     * 修改角色机构权限
     * @param roleId
     * @param orgIdList
     * @param operatorId
     * @return
     */
    int updateRoleOrgAuth(Integer roleId, List<Integer> orgIdList, Integer operatorId);

    /**
     * 根据角色ID查询角色自定义表列表
     * @param roleId
     * @param companyId
     * @return
     */
    List<CustomArchiveTable> searchCustomArchiveTableList(Integer roleId, Integer companyId);

    /**
     * 根据自定义表ID查询自定义字段列表
     * @param tableId
     * @return
     */
    List<CustomArchiveField> searchCustomArchiveTableFieldListByTableId(Integer tableId);

    /**
     * 根据角色ID查询角色自定义表字段列表
     * @param roleId
     * @param companyId
     * @return
     */
    List<CustomArchiveField> searchCustomArchiveTableFieldListByRoleId(Integer roleId, Integer companyId);

    /**
     * 修改角色自定义人员表字段权限
     * @param roleId
     * @param fieldIdList
     * @param operatorId
     * @return
     */
    int updateRoleCustomArchiveTableFieldAuth(Integer roleId, List<Integer> fieldIdList, Integer operatorId);

    /**
     * 保存数据级权限定义
     * @param roleDataLevelAuthVO
     * @return
     */
    int saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO);

}
