/**
 * 文件名：ApiAuthServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/25
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.qinjee.masterdata.service.auth.ApiAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/25
 */
@Service
public class ApiAuthServiceImpl implements ApiAuthService {
    @Override
    public int mergeOrg(List<Integer> oldOrgIdList, Integer newOrgId, Integer operatorId) {
        return 0;
    }

    @Override
    public int transferOrg(List<Integer> orgIdList, Integer parentOrgId, Integer operatorId) {
        return 0;
    }

    @Override
    public int deleteOrg(List<Integer> orgIdList, Integer operatorId) {
        return 0;
    }

    @Override
    public int deleteArchiveAuth(List<Integer> archiveIdList, Integer operatorId) {
        return 0;
    }
}
