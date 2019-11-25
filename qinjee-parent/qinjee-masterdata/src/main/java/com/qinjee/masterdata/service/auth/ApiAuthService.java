/**
 * 文件名：ApiAuthService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/25
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth;


import java.util.List;

/**
 * 为其它模块提供权限相关API Service
 * @author 周赟
 * @date 2019/11/25
 */
public interface ApiAuthService {

    /**
     * 机构合并
     * @param oldOrgIdList 旧机构列表
     * @param newOrgId 新机构
     * @param operatorId 操作人档案ID
     * @return
     */
    int mergeOrg(List<Integer> oldOrgIdList, Integer newOrgId, Integer operatorId);

    /**
     * 机构划转
     * @param orgIdList 当前机构ID
     * @param parentOrgId 父级机构ID
     * @param operatorId 操作人档案ID
     * @return
     */
    int transferOrg(List<Integer> orgIdList,Integer parentOrgId, Integer operatorId);

    /**
     * 新增机构
     * @param orgId 新增的机构ID
     * @param parentOrgId 父级机构ID
     * @param operatorId 操作人档案ID
     * @return
     */
    int addOrg(Integer orgId, Integer parentOrgId, Integer operatorId);

    /**
     * 删除机构
     * @param orgIdList 需要删除的机构ID
     * @param operatorId 操作人档案ID
     * @return
     */
    int deleteOrg(List<Integer> orgIdList, Integer operatorId);

    /**
     * 员工不在职权限删除(离职、退休)
     * @param archiveId 档案ID
     * @param operatorId 操作人档案ID
     * @return
     */
    int deleteArchiveAuth(Integer archiveId, Integer operatorId);
}
