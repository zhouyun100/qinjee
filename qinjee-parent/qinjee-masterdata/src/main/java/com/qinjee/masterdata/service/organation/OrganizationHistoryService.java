package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月17日 09:12:00
 */
public interface OrganizationHistoryService {

    /**
     * 新增机构历史信息
     * @return
     */
    ResponseResult addOrganizationHistory(OrganizationHistory organizationHistory);
}
