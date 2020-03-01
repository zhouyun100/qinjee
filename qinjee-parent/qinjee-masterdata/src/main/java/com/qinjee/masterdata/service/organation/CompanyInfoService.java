package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.CompanyInfo;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月16日 18:22:00
 */
public interface CompanyInfoService {
    /**
     * 根据企业id查询企业
     * @param companyId
     * @return
     */
    CompanyInfo getCompanyInfoById(Integer companyId);
}
