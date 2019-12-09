/**
 * 文件名：CustomTableFieldService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/28
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.custom;

import com.qinjee.masterdata.model.vo.custom.CheckCustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CheckCustomTableVO;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import java.util.List;
import java.util.Map;

/**
 * 自定义表字段接口
 * @date 2019/11/28
 * @author 周赟
 */
public interface CustomTableFieldService {

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

    /**
     * 根据企业ID和功能CODE查询自定义表
     * companyId:企业ID
     * funcCode:功能CODE
     * isEnable:是否启用(默认有效是1)
     * @param customTableVO
     * @return
     */
    List<CustomTableVO> searchCustomTableListByCompanyIdAndFuncCode(CustomTableVO customTableVO);

    /**
     * 根据企业ID和物理表名查询自定义组字段
     * @param companyId
     * @param tableCode
     * @return
     */
    CustomTableVO searchCustomTableGroupFieldListByTableCode(Integer companyId, String tableCode);

    /**
     * 根据表ID查询自定义组字段
     * @param tableId
     * @return
     */
    CustomTableVO searchCustomTableGroupFieldListByTableId(Integer tableId);


    /**
     * 处理自定义组字段数据回填
     * @param customTable 表单自定义表字段
     * @param mapValue 表单各字段数据值列表
     * @return
     */
    CustomTableVO handlerCustomTableGroupFieldList(CustomTableVO customTable,Map<Integer,String> mapValue);

    /**
     * 处理自定义组字段数据回填
     * @param customFieldList
     * @param mapValue
     */
    void handlerCustomTableGroupFieldList(List<CustomFieldVO> customFieldList,Map<Integer,String> mapValue);

    /**
     * 根据企业ID和功能CODE查询字段集合
     * @param companyId
     * @param funcCode
     * @return
     */
    List<CustomFieldVO> searchCustomFieldListByCompanyIdAndFuncCode(Integer companyId, String funcCode);

}
