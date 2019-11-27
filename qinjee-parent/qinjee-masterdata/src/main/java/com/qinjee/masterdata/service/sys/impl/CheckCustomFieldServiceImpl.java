/**
 * 文件名：CheckCustomFieldServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sys.impl;

import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.sys.CheckCustomTableVO;
import com.qinjee.masterdata.service.sys.CheckCustomFieldService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 周赟
 * @date 2019/11/22
 */
@Service
public class CheckCustomFieldServiceImpl implements CheckCustomFieldService {


    @Override
    public List<CheckCustomTableVO> checkCustomFieldValue(List<Integer> fileIdList, List<Map<Integer, Object>> mapList) {

        List<CheckCustomTableVO> customFieldValueList = new ArrayList<>();

        for(Map<Integer,Object> map : mapList){

        }
        return customFieldValueList;
    }
}
