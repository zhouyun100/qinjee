package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.CompanyInfoDao;
import com.qinjee.masterdata.model.entity.CompanyInfo;
import com.qinjee.masterdata.service.organation.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 18:23:00
 */
@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    private CompanyInfoDao companyInfoDao;

    @Override
    public CompanyInfo getCompanyInfoById(Integer companyId) {
        return companyInfoDao.selectByPrimaryKey(companyId);
    }
}
