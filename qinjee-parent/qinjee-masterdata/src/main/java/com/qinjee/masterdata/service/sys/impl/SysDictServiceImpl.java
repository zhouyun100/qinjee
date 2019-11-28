/**
 * 文件名：SysDictServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/22
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sys.impl;

import com.qinjee.masterdata.dao.sys.SysDictDao;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.service.sys.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/22
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictDao sysDictDao;

    @Override
    public List<SysDict> searchSysDictListByDictType(String dictType) {

        if(StringUtils.isBlank(dictType)){
            return null;
        }

        SysDict sysDict = new SysDict();
        sysDict.setDictType(dictType);
        List<SysDict> sysDictList = sysDictDao.searchSysDictList(sysDict);

        return sysDictList;
    }

    @Override
    public SysDict searchSysDictByTypeAndCode(String dictType, String dictCode) {
        SysDict sysDict = new SysDict();
        sysDict.setDictType(dictType);
        sysDict.setDictCode(dictCode);
        return sysDictDao.searchSysDictByTypeAndCode(sysDict);
    }
}
