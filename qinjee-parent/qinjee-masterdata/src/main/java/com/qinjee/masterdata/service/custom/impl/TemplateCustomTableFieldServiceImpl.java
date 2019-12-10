/**
 * 文件名：TemplateCustomTableFieldServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/9
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.custom.impl;

import com.qinjee.masterdata.dao.custom.TemplateCustomTableFieldDao;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableFieldVO;
import com.qinjee.masterdata.model.vo.custom.TemplateCustomTableVO;
import com.qinjee.masterdata.service.custom.CustomTableFieldService;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 周赟
 * @date 2019/12/9
 */
@Service
public class TemplateCustomTableFieldServiceImpl implements TemplateCustomTableFieldService {

    @Autowired
    private TemplateCustomTableFieldDao templateCustomTableFieldDao;

    @Autowired
    private CustomTableFieldService customTableFieldService;

    @Override
    public List<TemplateCustomTableVO> searchTableListByCompanyIdAndTemplateId(Integer companyId, Integer templateId, Integer isAll) {
        List<TemplateCustomTableVO> templateCustomTableList = templateCustomTableFieldDao.searchTableListByCompanyIdAndTemplateId(companyId,templateId,isAll);
        return templateCustomTableList;
    }

    @Override
    public List<TemplateCustomTableVO> searchTableFieldListByTemplateId(Integer templateId) {
        List<TemplateCustomTableVO> templateCustomTableList = templateCustomTableFieldDao.searchTableListByTemplateId(templateId);

        if(!CollectionUtils.isEmpty(templateCustomTableList)){
            List<TemplateCustomTableFieldVO> templateCustomTableFieldList = templateCustomTableFieldDao.searchTableFieldListByTemplateId(templateId);
            for(TemplateCustomTableVO templateCustomTable : templateCustomTableList){
                List<TemplateCustomTableFieldVO> fieldList = templateCustomTableFieldList.stream().filter(fieldVO -> {
                    if(fieldVO.getTableId().equals(templateCustomTable.getTableId())){
                        return true;
                    }else{
                        return false;
                    }
                }).collect(Collectors.toList());
                templateCustomTable.setFieldList(fieldList);
            }
        }

        return templateCustomTableList;
    }

    @Override
    public List<TemplateCustomTableFieldVO> searchFieldListByTableIdAndTemplateId(Integer tableId, Integer templateId) {
        List<TemplateCustomTableFieldVO> templateCustomTableFieldList = templateCustomTableFieldDao.searchFieldListByTableIdAndTemplateId(tableId, templateId);
        return templateCustomTableFieldList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveTemplateTableField(Integer templateId, List<TemplateCustomTableVO> templateCustomTableList, Integer operatorId) {
        int resultCount = 0;

        List<TemplateCustomTableVO> customTableList = templateCustomTableFieldDao.searchTableListByTemplateId(templateId);
        List<TemplateCustomTableVO> addTemplateTableList = new ArrayList<>();

        /**
         * 表单与DB数据对比，删除DB中多余的模板自定义表
         */
        if(!StringUtils.isEmpty(customTableList) && !StringUtils.isEmpty(templateCustomTableList)){
            List<TemplateCustomTableVO> delTemplateTableList = new ArrayList<>();
            for(TemplateCustomTableVO customTableDB : customTableList){

                for(TemplateCustomTableVO customTable : templateCustomTableList){
                    if(customTable.getTableId().equals(customTableDB.getTableId())){
                        delTemplateTableList.add(customTableDB);
                        addTemplateTableList.add(customTable);
                    }

                }
            }

            //删除DB中多余的表和字段
            customTableList.removeAll(delTemplateTableList);
            if(!CollectionUtils.isEmpty(customTableList)){
                resultCount += templateCustomTableFieldDao.deleteTemplateTable(templateId,customTableList,operatorId);
                resultCount += templateCustomTableFieldDao.deleteTemplateTableField(templateId,customTableList,operatorId);
            }
        }

        /**
         * 新增DB中不存在的表和字段
         */
        templateCustomTableList.removeAll(addTemplateTableList);
        if(!CollectionUtils.isEmpty(templateCustomTableList)){
            resultCount += templateCustomTableFieldDao.addTemplateTable(templateId,templateCustomTableList,operatorId);
            for(TemplateCustomTableVO customTable : templateCustomTableList){
                resultCount += templateCustomTableFieldDao.addTemplateTableField(templateId,customTable.getFieldList(),operatorId);
            }
        }

        /**
         * 更新表单中的自定义表及字段信息至DB
         */
        for(TemplateCustomTableVO templateCustomTableVO : templateCustomTableList){
            //修改自定义表
            resultCount += templateCustomTableFieldDao.updateTemplateTable(templateId, templateCustomTableVO, operatorId);

            //删除DB中原有模板表的所有字段(单表全量字段操作)
            resultCount += templateCustomTableFieldDao.deleteTemplateTableFieldByTemplateIdAndTableId(templateId, templateCustomTableVO.getTableId(), operatorId);

            //重新全量添加表单中的模板表字段信息(单表全量字段操作)
            resultCount += templateCustomTableFieldDao.addTemplateTableField(templateId,templateCustomTableVO.getFieldList(),operatorId);
        }
        return resultCount;
    }

    @Override
    public List<EntryRegistrationTableVO> searchCustomTableListByTemplateIdAndArchiveId(Integer templateId, Integer archiveId) {
        List<EntryRegistrationTableVO> entryRegistrationTableList = templateCustomTableFieldDao.searchEntryRegistrationTableListByTemplateId(templateId);

        for(EntryRegistrationTableVO tableVO : entryRegistrationTableList){
            List<CustomFieldVO> customFieldList = templateCustomTableFieldDao.searchCustomFieldListByTemplateIdAndTableId(templateId,tableVO.getTableId());
            tableVO.setCustomFieldVOList(customFieldList);
        }
        return entryRegistrationTableList;
    }

    @Override
    public List<EntryRegistrationTableVO> handlerCustomTableGroupFieldList(List<EntryRegistrationTableVO> entryRegistrationTableList, Map<Integer, String> mapValue) {
        if(!CollectionUtils.isEmpty(entryRegistrationTableList)){
            for(EntryRegistrationTableVO entryRegistrationTableVO : entryRegistrationTableList){
                List<CustomFieldVO> fieldList = entryRegistrationTableVO.getCustomFieldVOList();

                /**
                 * 处理自定义表字段数据回填
                 */
                customTableFieldService.handlerCustomTableGroupFieldList(fieldList,mapValue);
            }
        }
        return entryRegistrationTableList;
    }
}
