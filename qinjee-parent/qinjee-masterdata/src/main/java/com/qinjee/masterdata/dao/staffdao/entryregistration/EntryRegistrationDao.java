/**
 * 文件名：EntryRegistrationDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.staffdao.entryregistration;


import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/12/10
 */
@Repository
public interface EntryRegistrationDao {

    /**
     * 根据企业ID查询入职登记模板列表
     * @param companyId
     * @return
     */
    List<TemplateEntryRegistration> searchTemplateEntryRegistrationList(Integer companyId);

    /**
     * 新增入职登记模板
     * @param templateEntryRegistration
     * @return
     */
    int addTemplateEntryRegistration(TemplateEntryRegistration templateEntryRegistration);

    /**
     * 删除入职登记模板
     * @param templateId
     * @return
     */
    int deleteTemplateEntryRegistration(Integer templateId, Integer operatorId);

    /**
     * 修改入职登记模板
     * @param templateEntryRegistration
     * @return
     */
    int modifyTemplateEntryRegistration(TemplateEntryRegistration templateEntryRegistration);

    /**
     * 根据模板ID获取模板详情
     * @param templateId
     * @return
     */
    TemplateEntryRegistration getTemplateEntryRegistrationByTemplateId(Integer templateId);

    String searchLogurlByComanyIdAnadTemplateId(@Param("templateId") Integer templateId, @Param("companyId") Integer companyId);
}
