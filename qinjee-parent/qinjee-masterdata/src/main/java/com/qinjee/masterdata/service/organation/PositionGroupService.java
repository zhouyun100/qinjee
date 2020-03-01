package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 09:39:00
 */
public interface PositionGroupService {
    /**
     * 获取所有的职位族
     * @return
     */
    ResponseResult<PageResult<PositionGroup>> getAllPositionGroup(UserSession userSession, PageVo pageVo);

    /**
     * 新增职位族
     * @param userSession
     * @param positionGroupName
     */
    ResponseResult addPositionGroup(UserSession userSession, String positionGroupName);

    /**
     * 编辑职位族
     * @param positionGroupVo
     */
    ResponseResult editPositionGroup(UserSession userSession, PositionGroupVo positionGroupVo);

    /**
     * 删除职位族
     * @param positionGroupIds
     */
    int deletePositionGroup(List<Integer> positionGroupIds);

    /**
     * 职位族排序
     * @return
     */
    ResponseResult sortPositionGroup(LinkedList<String> positionGroupIds);

    /**
     * 树形展示职位
     * @return
     */
    ResponseResult<List<PositionGroup>> getAllPositionGroupTree(UserSession userSession);

    /**
     * 导出用户下所有职位族，根据企业id
     * @param filePath
     * @param userSession
     * @return
     */
    ResponseResult downloadAllPositionGroupToExcel(String filePath, UserSession userSession);

    /**
     * 导出选中的职位族
     * @param filePath
     * @param positionGroupIds
     * @param userSession
     * @return
     */
    ResponseResult downloadPositionGroupToExcelByOrgId(String filePath, List<Integer> positionGroupIds, UserSession userSession);
}
