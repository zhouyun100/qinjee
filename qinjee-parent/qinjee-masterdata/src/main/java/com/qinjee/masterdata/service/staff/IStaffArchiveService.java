package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.entity.ArchiveCareerTrack;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.lang.reflect.InvocationTargetException;
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
     * @param userSession
     * @return
     */
    List<QuerySchemeList> selectQueryScheme(UserSession userSession);


    void saveQueryScheme(QueryArcVo queryArcVo);

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
     * @param userArchiveVo
     * @return
     */
    void updateArchive(UserArchiveVo userArchiveVo,UserSession userSession);

    /**
     * 查看人员档案
     * @param userSession
     * @return
     */
    UserArchiveVo selectArchive(UserSession userSession);

    /**
     * 新增档案
     * @param userArchiveVo
     * @return
     */
   void insertArchive(UserArchiveVo userArchiveVo,UserSession userSession);
    /**
     * 修改预入职信息(显示字段的信息)
     */
    void updateArchiveField(Map<Integer, String> map);
    /**
     * 查看档案（查询某个组织部门下的档案）
     */
    PageResult<UserArchiveVo> selectArchivebatch(UserSession userSession, Integer companyId, Integer pageSize, Integer currentPage);
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
     * @return
     */
    ExportFile selectArchiveByQueryScheme( UserSession userSession, List<Integer> archiveIdList) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 显示权限下，对应表的字段
     * @param tableId
     * @param userSession
     * @return
     */
    List<String> selectFieldByTableIdAndAuth(Integer tableId, UserSession userSession);

    /**
     * 显示权限下档案表的字段
     * @param userSession
     * @return
     */
    List<String> selectFieldByArcAndAuth(UserSession userSession);

    /**
     * @param id
     * @return
     */
    List<ArchiveCareerTrack> selectCareerTrack(Integer id);

    /**
     * @param archiveCareerTrackVo
     * @param userSession
     */
    void updateCareerTrack(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) throws IllegalAccessException;

    /**
     *
     * @param archiveCareerTrackVo
     * @param userSession
     */
    void insertCareerTrack(ArchiveCareerTrackVo archiveCareerTrackVo, UserSession userSession) throws IllegalAccessException;

    /**
     *
     * @param id
     */
    void deleteCareerTrack(Integer id);

    UserArchiveVo selectArchiveSingle(Integer id);

    List<UserArchiveVo> selectUserArchiveByName(String name);

    void setDefaultQuerySchme(Integer querySchmeId,UserSession userSession);
}
