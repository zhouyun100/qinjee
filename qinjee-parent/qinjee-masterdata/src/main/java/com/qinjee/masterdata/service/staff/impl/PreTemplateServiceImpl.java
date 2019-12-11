package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.model.entity.TemplateEntryRegistration;
import com.qinjee.masterdata.model.vo.custom.EntryRegistrationTableVO;
import com.qinjee.masterdata.service.custom.TemplateCustomTableFieldService;
import com.qinjee.masterdata.service.staff.EntryRegistrationService;
import com.qinjee.masterdata.service.staff.IPreTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PreTemplateServiceImpl implements IPreTemplateService {
    @Autowired
    private EntryRegistrationService entryRegistrationService;
    @Autowired
    private TemplateCustomTableFieldService templateCustomTableFieldService;
    @Autowired
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private StaffCommonServiceImpl staffCommonService;


    @Override
    public List < EntryRegistrationTableVO > handlerCustomTableGroupFieldList(Integer companyId, Integer preId) throws IllegalAccessException {
        Map < Integer, String > map =new HashMap <> (  );
        //根据企业id确定模板id
        List < TemplateEntryRegistration > templateEntryRegistrations =
                entryRegistrationService.searchTemplateEntryRegistrationList ( companyId );
        Integer templateId = templateEntryRegistrations.get ( 0 ).getTemplateId ();
        //根据模板id与预入职id获取模板数据
        List < EntryRegistrationTableVO > entryRegistrationTableVOS1 =
                templateCustomTableFieldService.searchCustomTableListByTemplateIdAndArchiveId ( templateId, preId );
        //根据企业id找到tableId
       List<Integer> list=customTableFieldDao.selectTableIdByCompanyIdAndFuncCode(companyId,"PRE");
       //找到对应的值
        for (Integer integer : list) {
            Map < Integer, String > map1 = staffCommonService.selectValue ( integer, preId );
            for (Map.Entry < Integer, String > integerStringEntry : map1.entrySet ()) {
                map.put ( integerStringEntry.getKey (),integerStringEntry.getValue () );
            }

        }
        List < EntryRegistrationTableVO > entryRegistrationTableVOS =
                templateCustomTableFieldService.handlerCustomTableGroupFieldList ( entryRegistrationTableVOS1, map );
        return entryRegistrationTableVOS;
    }


}
