/**
 * 文件名：CompanyRegistServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2020/1/2
 * <p>
 * Copyright(C) 2020, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.userinfo.impl;

import com.qinjee.masterdata.controller.userinfo.CompanyRegistController;
import com.qinjee.masterdata.dao.userinfo.CompanyRegistDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import com.qinjee.masterdata.service.userinfo.CompanyRegistService;
import com.qinjee.masterdata.service.userinfo.UserLoginService;
import com.qinjee.utils.MD5Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 注册企业
 * @author 周赟
 * @date 2020/01/02
 */
@Service
public class CompanyRegistServiceImpl implements CompanyRegistService {

    private static Logger logger = LogManager.getLogger(CompanyRegistServiceImpl.class);

    @Autowired
    private CompanyRegistDao companyRegistDao;

    @Autowired
    private UserLoginService userLoginService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registCompany(String companyName, Integer userNumber, Date validEndDate, String phone, String userName) {

        logger.info("REGIST START!");

        //添加企业信息
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(companyName);
        companyInfo.setUserNumber(userNumber);
        companyInfo.setValidEndDate(validEndDate);
        companyRegistDao.addCompany(companyInfo);
        Integer companyId = companyInfo.getCompanyId();
        logger.info("companyId={}" , companyId);

        if(companyId != null){

            UserInfo userInfo = userLoginService.searchUserInfoDetailByPhone(phone);
            if(userInfo == null){
                //添加登录用户信息
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                userInfo.setUserName(userName);
                int phoneLength = phone.length();
                userInfo.setPassword(MD5Utils.getMd5(phone.substring(phoneLength-6,phoneLength)));
                companyRegistDao.addUserInfo(userInfo);
            }else{
                if(StringUtils.isBlank(userInfo.getUserName())){
                    userInfo.setUserName(userName);
                }
            }
            Integer userId = userInfo.getUserId();
            logger.info("userId={}" , userId);

            if(userId != null){
                //添加档案
                UserArchive userArchive = new UserArchive();
                userArchive.setUserId(userId);
                userArchive.setUserName(userName);
                userArchive.setCompanyId(companyId);
                companyRegistDao.addUserArchive(userArchive);
                Integer archiveId = userArchive.getArchiveId();
                logger.info("archiveId={}" , archiveId);

                //添加登录用户与企业关系
                UserCompany userCompany = new UserCompany();
                userCompany.setCompanyId(companyId);
                userCompany.setUserId(userId);
                userCompany.setIsEnable(new Short("1"));
                companyRegistDao.addUserCompany(userCompany);

                //添加企业邮箱配置，默认使用勤杰邮件配置
                EmailConfig emailConfig = new EmailConfig();
                emailConfig.setCompanyId(1);
                companyRegistDao.addEmailConfig(emailConfig);

                //添加企业顶级机构，以企业命名
                Organization organization = new Organization();
                organization.setOrgName(companyName);
                organization.setCompanyId(companyId);
                companyRegistDao.addOrganization(organization);

                //添加系统管理员角色（内置）
                Role role = new Role();
                role.setCompanyId(companyId);
                companyRegistDao.addRole(role);
                Integer roleId = role.getRoleId();
                logger.info("roleId={}" , roleId);

                if(roleId != null && archiveId != null){
                    //添加档案与角色关系
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(roleId);
                    userRole.setArchiveId(archiveId);
                    companyRegistDao.addUserRole(userRole);
                }

                //初始化角色机构权限
                companyRegistDao.initRoleOrgAuth(companyId);

                //初始化角色菜单权限
                companyRegistDao.initRoleMenuAuth(companyId);

                //初始化企业菜单权限
                companyRegistDao.initCompanyMenu(companyId);

                //默认查询勤杰的自定义表
                List<CustomArchiveTable> customArchiveTableList = companyRegistDao.searchCustomArchiveTable(1);
                for(CustomArchiveTable customArchiveTable : customArchiveTableList){
                    Integer oldTableId = customArchiveTable.getTableId();
                    customArchiveTable.setCompanyId(companyId);
                    companyRegistDao.addCustomTable(customArchiveTable);
                    int tableId = customArchiveTable.getTableId();
                    logger.info("tableId={}" , tableId);

                    List<CustomArchiveGroup> customArchiveGroupList = companyRegistDao.searchCustomArchiveGroup(oldTableId);
                    if(CollectionUtils.isNotEmpty(customArchiveGroupList)){
                        for(CustomArchiveGroup customArchiveGroup : customArchiveGroupList){
                            Integer oldGroupId = customArchiveGroup.getGroupId();
                            customArchiveGroup.setTableId(tableId);
                            companyRegistDao.addCustomGroup(customArchiveGroup);
                            Integer groupId = customArchiveGroup.getGroupId();
                            logger.info("groupId={}" , groupId);

                            List<CustomArchiveField> customArchiveFieldList = companyRegistDao.searchCustomArchiveField(oldTableId,oldGroupId);
                            for(CustomArchiveField customArchiveField : customArchiveFieldList){
                                customArchiveField.setTableId(tableId);
                                customArchiveField.setGroupId(groupId);
                                companyRegistDao.addCustomField(customArchiveField);
                                Integer fieldId = customArchiveField.getFieldId();
                                logger.info("fieldId={}" , fieldId);
                                companyRegistDao.addRoleCustomFieldAuth(roleId,fieldId);
                            }
                        }
                    }else{
                        List<CustomArchiveField> customArchiveFieldList = companyRegistDao.searchCustomArchiveField(oldTableId,null);
                        for(CustomArchiveField customArchiveField : customArchiveFieldList){
                            customArchiveField.setTableId(tableId);
                            companyRegistDao.addCustomField(customArchiveField);
                            Integer fieldId = customArchiveField.getFieldId();
                            logger.info("fieldId={}" , fieldId);
                            companyRegistDao.addRoleCustomFieldAuth(roleId,fieldId);
                        }
                    }
                }

                //添加企业预入职模板数据
                TemplateEntryRegistration templateEntryRegistration = new TemplateEntryRegistration();
                templateEntryRegistration.setCompanyId(companyId);
                companyRegistDao.addTemplateEntryRegistration(templateEntryRegistration);
                Integer templateId = templateEntryRegistration.getTemplateId();
                logger.info("templateId={}" , templateId);

                //添加企业预入职模板附件数据
                TemplateAttachmentGroup templateAttachmentGroup = new TemplateAttachmentGroup();
                templateAttachmentGroup.setTemplateId(templateId);
                companyRegistDao.addTemplateAttachmentGroup(templateAttachmentGroup);

                //添加企业预入职模板自定义表
                TemplateCustomTableVO templateCustomTableVO = new TemplateCustomTableVO();
                templateCustomTableVO.setTemplateId(templateId);
                companyRegistDao.addTemplateCustomTable(templateCustomTableVO);

                //添加企业预入职模板自定义表字段
                TemplateCustomTableFieldVO templateCustomTableFieldVO = new TemplateCustomTableFieldVO();
                templateCustomTableFieldVO.setTemplateId(templateId);
                companyRegistDao.addTemplateCustomField(templateCustomTableFieldVO);
            }
        }
        logger.info("REGIST FINISHED!");
    }
}
