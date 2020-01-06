/**
 * 文件名：TemplateAttachmentGroupDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.staffdao.entryregistration;

import com.qinjee.masterdata.model.entity.TemplateAttachmentGroup;
import com.qinjee.masterdata.model.vo.staff.entryregistration.TemplateAttachmentGroupVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/12/10
 */
@Repository
public interface TemplateAttachmentGroupDao {

    /**
     * 根据模板ID查询模板附件配置列表
     * @param templateId 模板ID
     * @param isAll 是否显示全部(0：是[包含系统默认且未配置的信息]，1：否[仅显示模板已配置的附件信息])，默认是0，显示全部
     * @return
     */
    List<TemplateAttachmentGroupVO> searchTemplateAttachmentListByTemplateId(Integer templateId, Integer isAll);

    /**
     * 根据主键ID查询模板附件详情
     * @param tagId
     * @return
     */
    TemplateAttachmentGroupVO getTemplateAttachmentListByTagId(Integer tagId);

    /**
     * 修改模板附件信息
     * @param templateAttachmentGroup
     * @return
     */
    int modifyTemplateAttachmentGroup(TemplateAttachmentGroup templateAttachmentGroup);

    /**
     * 删除模板附件信息
     * @param tagId
     * @param operatorId
     * @return
     */
    int delTemplateAttachmentGroup(Integer tagId, Integer operatorId);

    /**
     * 新增模板附件信息
     * @param templateAttachmentGroup
     * @return
     */
    int addTemplateAttachmentGroup(TemplateAttachmentGroup templateAttachmentGroup);

    /**
     * 模板附件排序
     * @param templateAttachmentGroupList
     * @return
     */
    int sortTemplateAttachmentGroup(List<TemplateAttachmentGroup> templateAttachmentGroupList, Integer operatorId);

    /**
     * 批量新增附件
     * @param list
     * @param operatorId
     * @return
     */
    int addTemplateAttachmentGroupBatch(List<TemplateAttachmentGroup> list, Integer operatorId);

    /**
     * 批量删除模板附件信息
     * @param list
     * @param operatorId
     * @return
     */
    int delTemplateAttachmentGroupList(List<TemplateAttachmentGroupVO> list, Integer operatorId);
}
