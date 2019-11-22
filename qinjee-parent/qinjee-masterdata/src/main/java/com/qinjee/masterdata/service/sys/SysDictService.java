/**
 * 文件名：SysDictService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/22
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sys;

import com.qinjee.masterdata.model.entity.SysDict;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/22
 */
public interface SysDictService {

    /**
     * 根据字典类型查询字典列表
     * @param dictType
     * @return
     */
    List<SysDict> searchSysDictListByDictType(String dictType);
}
