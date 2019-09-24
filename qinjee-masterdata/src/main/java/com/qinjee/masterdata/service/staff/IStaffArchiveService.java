package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

import java.util.List;

/**
 * @author Administrator
 */
public interface IStaffArchiveService {
    /**
     * 新增人员岗位关系表
     * @param userArchivePostRelation
     * @return
     */

    ResponseResult insertUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation);

    /**
     * 逻辑删除人员岗位关系表
     * @param list
     * @return
     */
    ResponseResult deleteUserArchivePostRelation(List<Integer> list);

    /**
     * 更新人员岗位关系表
     * @param userArchivePostRelation
     * @return
     */
    ResponseResult updateUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation);

    /**
     * 分页查询人员岗位关系表
     * @param currentPage
     * @param pageSize
     * @param list
     * @return
     */
    ResponseResult<PageResult<UserArchivePostRelation>> selectUserArchivePostRelation(Integer currentPage,Integer pageSize,
                                                                                      List<Integer> list);

    /**
     * 逻辑删除查询方案
     * @param list
     * @return
     */
    ResponseResult deleteQueryScheme(List<Integer> list);

    /**
     * 展示排序方案的显示字段与排序字段
     * @param id
     * @return
     */
    ResponseResult<QuerySchemeList> selectQueryScheme(Integer id);

    /**
     * 保存查询方案
     * @param queryScheme
     * @param querySchemeFieldlist
     * @param querySchemeSortlist
     * @return
     */
    ResponseResult saveQueryScheme(QueryScheme queryScheme, List<QuerySchemeField> querySchemeFieldlist, List<QuerySchemeSort> querySchemeSortlist);

    /**
     * 逻辑删除档案
     * @param archiveid
     * @return
     */
    ResponseResult deleteArchiveById(Integer archiveid);
    /**
     * 恢复删除档案
     * @param archiveid
     * @return
     */
    ResponseResult resumeDeleteArchiveById(Integer archiveid);

    /**
     * 更新档案
     * @param userArchive
     * @return
     */
    ResponseResult updateArchive(UserArchive userArchive);

    /**
     * 查看档案
     * @param archiveid
     * @return
     */
    ResponseResult selectArchive(Integer archiveid);

    /**
     * 新增档案
     * @param userArchive
     * @return
     */
    ResponseResult insertArchive(UserArchive userArchive);
}
