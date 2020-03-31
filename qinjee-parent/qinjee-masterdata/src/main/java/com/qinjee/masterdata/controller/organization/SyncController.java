package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.dao.CompanyInfoDao;
import com.qinjee.masterdata.dao.custom.CustomTableFieldDao;
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/syncCustom")
@RestController
@Api(tags = "【自定义表同步接口】")
public class SyncController extends BaseController {

    @Autowired
    private CustomTableFieldDao customTableFieldDao;
    @Autowired
    private CompanyInfoDao  companyInfoDao;

    @ApiOperation(value = "ok，同步用户自定义档案字段表", notes = "彭洪思")
    @PostMapping("/syncCustomArchiveField")
    @Transactional
    public ResponseResult syncCustomArchiveField() {

        long start = System.currentTimeMillis();
        //查出平台下的tableId
        List<Integer> tableIds = customTableFieldDao.selectTableIdByCompanyId(1);

        List<Integer> companyIds =  companyInfoDao.selectCompanyIds();
        //去除平台企业id
        companyIds.remove(1);

        //根据平台下的tableId遍历执行，查询自定义字段列表（拿到fieldName和fieldCode）
        for (Integer tableId : tableIds) {
            List<CustomArchiveField> archiveFields = customTableFieldDao.selectFieldByTableId(tableId);
            //查询除了平台外的企业的tableId，根据tableId + fieldName更新fieldCode
            List<CustomArchiveField> otherFields=customTableFieldDao.selectFieldByCompanyIds(companyIds);
            for (CustomArchiveField archiveField : archiveFields) {
                for (CustomArchiveField otherField : otherFields) {
                    if(archiveField.getFieldName().equals(otherField.getFieldName())){
                        customTableFieldDao.updateFieldCodeByTableIdAndFieldName(otherField.getTableId(),otherField.getFieldName(),archiveField.getFieldCode(),archiveField.getIsSystemDefine());

                    }

                }

            }
        }
        long end = System.currentTimeMillis();
        System.out.println("同步耗时："+(end-start)+"毫秒");

        return null;

    }
}
