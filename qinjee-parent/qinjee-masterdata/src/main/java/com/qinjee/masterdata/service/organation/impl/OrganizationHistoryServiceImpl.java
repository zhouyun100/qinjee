package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.organation.OrganizationHistoryDao;
import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月17日 09:12:00
 */
@Service
public class OrganizationHistoryServiceImpl implements OrganizationHistoryService {

    @Autowired
    private OrganizationHistoryDao organizationHistoryDao;

    @Override
    public ResponseResult addOrganizationHistory(OrganizationHistory organizationHistory) {
        int insert = organizationHistoryDao.saveOrganizationHistory(organizationHistory);
        return insert == 1 ? new ResponseResult(CommonCode.SUCCESS) : new ResponseResult(CommonCode.FAIL);
    }

}
