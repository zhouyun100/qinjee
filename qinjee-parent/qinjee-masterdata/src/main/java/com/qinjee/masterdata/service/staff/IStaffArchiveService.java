package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.masterdata.model.vo.staff.UserArchivePostRelationVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface IStaffArchiveService {
    /**
     * 新增人员岗位关系表
     * @param userArchivePostRelationVo
     * @return
     */

    void insertUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo, UserSession userSession);

    /**
     * 逻辑删除人员岗位关系表
     * @param list
     * @return
     */
    void deleteUserArchivePostRelation(List<Integer> list) throws Exception;

    /**
     * 更新人员岗位关系表
     * @param userArchivePostRelation
     * @return
     */
    void updateUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation);

    /**
     * 分页查询人员岗位关系表
     * @param currentPage
     * @param pageSize
     * @param list
     * @return
     */
   PageResult<UserArchivePostRelation> selectUserArchivePostRelation(Integer currentPage, Integer pageSize,
                                                                     List<Integer> list);

    /**
     * 逻辑删除查询方案
     * @param list
     * @return
     */
    void deleteQueryScheme(List<Integer> list) throws Exception;

    /**
     * 展示排序方案的显示字段与排序字段
     * @param id
     * @return
     */
    QuerySchemeList selectQueryScheme(Integer id);

    /**
     * 保存查询方案
     * @param queryScheme
     * @param querySchemeFieldlist
     * @param querySchemeSortlist
     * @return
     */
    void saveQueryScheme(QueryScheme queryScheme, List<QuerySchemeField> querySchemeFieldlist, List<QuerySchemeSort> querySchemeSortlist);

    /**
     * 逻辑删除档案
     * @param archiveid
     * @return
     */
    void deleteArchiveById(List<Integer> archiveid) throws Exception;
    /**
     * 恢复删除档案
     * @param archiveid
     * @return
     */
   void resumeDeleteArchiveById(Integer archiveid) throws Exception;

    /**
     * 更新档案
     * @param userArchive
     * @return
     */
    void updateArchive(UserArchive userArchive);

    /**
     * 查看人员档案
     * @param userSession
     * @return
     */
    UserArchive selectArchive(UserSession userSession);

    /**
     * 新增档案
     * @param userArchive
     * @return
     */
   void insertArchive(UserArchive userArchive);
    /**
     * 修改预入职信息(显示字段的信息)
     */
    void updateArchiveField(Map<Integer, String> map);
    /**
     * 查看档案（查询某个组织部门下的档案）
     */
    PageResult<UserArchive> selectArchivebatch(UserSession userSession, Integer companyId);
    /**
     * 通过id找到人员姓名与工号
     */
    Map<String, String> selectNameAndNumber(Integer id);
    /**
     * 通过id查询到对应机构名称
     */
    String selectOrgName(Integer id);

    /**
     * 根据显示方案展示人员信息
     * @param schemeId
     * @param orgId
     * @return
     */
   PageResult<UserArchive> selectArchiveByQueryScheme(Integer schemeId, Integer orgId);
}