/**
 * 文件名：EntryRegistrationServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.staffdao.entryregistration.EntryRegistrationDao;
import com.qinjee.masterdata.dao.staffdao.entryregistration.TemplateAttachmentGroupDao;
import com.qinjee.masterdata.model.entity.TemplateAttachmentGroup;
import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import com.qinjee.masterdata.model.vo.staff.entryregistration.TemplateAttachmentGroupVO;
import com.qinjee.masterdata.service.staff.EntryRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/12/10
 */
@Service
public class EntryRegistrationServiceImpl implements EntryRegistrationService {

    @Autowired
    private EntryRegistrationDao entryRegistrationDao;

    @Autowired
    private TemplateAttachmentGroupDao templateAttachmentGroupDao;

    @Override
    public List<TemplateEntryRegistration> searchTemplateEntryRegistrationList(Integer companyId) {
        List<TemplateEntryRegistration> templateEntryRegistrationList = entryRegistrationDao.searchTemplateEntryRegistrationList(companyId);
        return templateEntryRegistrationList;
    }

    @Override
    public int addTemplateEntryRegistration(TemplateEntryRegistration templateEntryRegistration) {
        int resultCount = 0;
        resultCount += entryRegistrationDao.addTemplateEntryRegistration(templateEntryRegistration);
        return resultCount;
    }

    @Override
    public int deleteTemplateEntryRegistration(Integer templateId, Integer operatorId) {
        int resultCount = 0;
        resultCount += entryRegistrationDao.deleteTemplateEntryRegistration(templateId, operatorId);
        return resultCount;
    }

    @Override
    public int modifyTemplateEntryRegistration(TemplateEntryRegistration templateEntryRegistration) {
        int resultCount = 0;
        resultCount += entryRegistrationDao.modifyTemplateEntryRegistration(templateEntryRegistration);
        return resultCount;
    }

    @Override
    public TemplateEntryRegistration getTemplateEntryRegistrationByTemplateId(Integer templateId) {
        TemplateEntryRegistration templateEntryRegistration = entryRegistrationDao.getTemplateEntryRegistrationByTemplateId(templateId);
        return templateEntryRegistration;
    }

    @Override
    public List<TemplateAttachmentGroupVO> searchTemplateAttachmentListByTemplateId(Integer templateId, Integer isAll) {
        List<TemplateAttachmentGroupVO> templateAttachmentGroupList = templateAttachmentGroupDao.searchTemplateAttachmentListByTemplateId(templateId,isAll);
        return templateAttachmentGroupList;
    }

    @Override
    public TemplateAttachmentGroupVO getTemplateAttachmentListByTagId(Integer tagId) {
        TemplateAttachmentGroupVO templateAttachmentGroup = templateAttachmentGroupDao.getTemplateAttachmentListByTagId(tagId);
        return templateAttachmentGroup;
    }

    @Override
    public int modifyTemplateAttachmentGroup(TemplateAttachmentGroup templateAttachmentGroup) {
        int resultCount = 0;
        resultCount += templateAttachmentGroupDao.modifyTemplateAttachmentGroup(templateAttachmentGroup);
        return resultCount;
    }

    @Override
    public int addTemplateAttachmentGroup(TemplateAttachmentGroup templateAttachmentGroup) {
        int resultCount = 0;
        resultCount += templateAttachmentGroupDao.addTemplateAttachmentGroup(templateAttachmentGroup);
        return resultCount;
    }

    @Override
    public int delTemplateAttachmentGroup(Integer tagId, Integer operatorId) {
        int resultCount = 0;
        resultCount += templateAttachmentGroupDao.delTemplateAttachmentGroup(tagId, operatorId);
        return resultCount;
    }

    @Override
    public int sortTemplateAttachmentGroup(List<TemplateAttachmentGroup> templateAttachmentGroupList, Integer operatorId) {
        int resultCount = 0;
        resultCount += templateAttachmentGroupDao.sortTemplateAttachmentGroup(templateAttachmentGroupList, operatorId);
        return resultCount;
    }
}
