/**
 * 文件名：CheckCustomFieldService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sys;

import com.qinjee.masterdata.model.vo.sys.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.sys.CheckCustomTableVO;

import java.util.List;
import java.util.Map;

/**
 * 检验自定义字段
 * @author 周赟
 * @date 2019/11/26
 */
public interface CheckCustomFieldService {

    /**
     * 校验自定义表字段的值
     * @param fileIdList 自定义字段ID列表
     * @param mapList 自定义字段数据集列表
     * @return
     */
    List<CheckCustomTableVO> checkCustomFieldValue(List<Integer> fileIdList, List<Map<Integer,Object>> mapList);

    /**
     * 校验自定义字段值（单个校验）
     * @param checkCustomFieldVO
     * @return
     */
    void validCustomFieldValue(CheckCustomFieldVO checkCustomFieldVO);
}
