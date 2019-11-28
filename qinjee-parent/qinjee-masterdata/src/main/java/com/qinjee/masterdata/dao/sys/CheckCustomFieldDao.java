/**
 * 文件名：CheckCustomFieldDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/27
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.sys;

import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统字典表
 * @author 周赟
 * @date 2019/11/22
 */
@Repository
public interface CheckCustomFieldDao {

    /**
     * 根据字段ID列表查询各字段详细属性信息
     * @param fileIdList
     * @return
     */
    List<CheckCustomFieldVO> searchCustomFieldList(List<Integer> fileIdList);
}
