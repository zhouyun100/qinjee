/**
 * 文件名：RoleSearchDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/19
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.sys;

import com.qinjee.masterdata.model.entity.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统字典表
 * @author 周赟
 * @date 2019/11/22
 */
@Repository
public interface SysDictDao {

    /**
     * 查询字典列表
     * @param sysDict
     * @return
     */
    List<SysDict> searchSysDictList(SysDict sysDict);

    /**
     * 查询字典对象
     * @param sysDict
     * @return
     */
    SysDict searchSysDictByTypeAndCode(SysDict sysDict);

}
