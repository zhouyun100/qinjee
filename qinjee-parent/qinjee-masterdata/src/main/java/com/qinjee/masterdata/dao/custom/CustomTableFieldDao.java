/**
 * 文件名：CustomTableFieldDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/28
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.custom;

import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomGroupVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/28
 */
@Repository
public interface CustomTableFieldDao {

    /**
     * 根据字段ID列表查询各字段详细属性信息
     * @param fileIdList
     * @return
     */
    List<CustomFieldVO> searchCustomFieldListByFieldIdList(List<Integer> fileIdList);

    /**
     * 查询对应业务模块的自定义表
     * @param customTableVO
     * @return
     */
    List<CustomTableVO> searchCustomTableListByCompanyIdAndFuncCode(CustomTableVO customTableVO);

    /**
     * 查询表对应的组信息列表
     * @param companyId 企业ID
     * @param tableCode 表CODE
     * @param tableId 表ID
     * @return
     */
    List<CustomGroupVO> searchCustomGroupList(Integer companyId, String tableCode, Integer tableId);

    /**
     * 查询字段信息列表
     * @param companyId 企业ID
     * @param tableCode 表CODE
     * @param tableId 表ID
     * @return
     */
    List<CustomFieldVO> searchCustomFieldList(Integer companyId, String tableCode, Integer tableId);

    /**
     * 根据企业ID和功能CODE查询字段集合
     * @param companyId
     * @param funcCode
     * @return
     */
    List<CustomFieldVO> searchCustomFieldListByCompanyIdAndFuncCode(@Param("companyId") Integer companyId, @Param("funcCode") String funcCode);
}
