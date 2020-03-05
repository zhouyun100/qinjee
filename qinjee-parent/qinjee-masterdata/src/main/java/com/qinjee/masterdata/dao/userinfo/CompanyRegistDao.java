/**
 * 文件名：UserLoginDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.userinfo;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author
 * @date 2019/9/18
 */
@Repository
public interface CompanyRegistDao {


    /**
     * 根据手机号查询注册的企业数量
     * @param phone
     * @return
     */
    Integer searchRegistCompanyCountByPhone(String phone);

    int addCompany(CompanyInfo companyInfo);

    int addUserInfo(UserInfo userInfo);

    int addUserArchive(UserArchive userArchive);

    int addUserCompany(UserCompany userCompany);

    int addEmailConfig(EmailConfig emailConfig);

    int addOrganization(Organization organization);

    int addRole(Role role);

    int addUserRole(UserRole userRole);

    int initRoleOrgAuth(Integer companyId);

    int initRoleMenuAuth(Integer companyId);

    int initCompanyMenu(Integer companyId);

    List<CustomArchiveTable> searchCustomArchiveTable(Integer companyId);

    List<CustomArchiveGroup> searchCustomArchiveGroup(Integer tableId);

    List<CustomArchiveField> searchCustomArchiveField(Integer tableId,Integer groupId);

    int addCustomTable(CustomArchiveTable customArchiveTable);

    int addCustomGroup(CustomArchiveGroup customGroup);

    int addCustomField(CustomArchiveField customField);

    int addRoleCustomFieldAuth(Integer roleId, Integer fieldId);

    int addTemplateEntryRegistration(TemplateEntryRegistration templateEntryRegistration);

    int addTemplateAttachmentGroup(TemplateAttachmentGroup templateAttachmentGroup);

    int addTemplateCustomTable(TemplateCustomTableVO templateCustomTableVO);

    int addTemplateCustomField(TemplateCustomTableFieldVO templateCustomTableFieldVO);
}
