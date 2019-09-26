package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.QuerySchemeList;
import com.qinjee.masterdata.model.vo.staff.UserArchivePostRelationVo;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】档案相关接口")
public class StaffArchiveController extends BaseController {
    @Autowired
    private IStaffArchiveService staffArchiveService;

    /**
     * 新增档案表
     */
    @RequestMapping(value = "/insertArchive", method = RequestMethod.POST)
    @ApiOperation(value = "新增档案表", notes = "hkt")
    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult insertArchive(UserArchive userArchive) {

        return staffArchiveService.insertArchive(userArchive);
    }

    /**
     * 批量删除档案
     */
    @RequestMapping(value = "/deleteArchiveById", method = RequestMethod.POST)
    @ApiOperation(value = "删除档案", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "人员档案id集合", paramType = "query", required = true)
    public ResponseResult deleteArchiveById(List<Integer> archiveid) {

        return staffArchiveService.deleteArchiveById(archiveid);
    }
    /**
     * 删除恢复
     */
    @RequestMapping(value = "/resumeDeleteArchiveById", method = RequestMethod.POST)
    @ApiOperation(value = "恢复删除档案", notes = "hkt")
    @ApiImplicitParam(name = "Archiveid", value = "人员档案id", paramType = "query", required = true)
    public ResponseResult resumeDeleteArchiveById(Integer archiveid) {

        return staffArchiveService.resumeDeleteArchiveById(archiveid);
    }
    /**
     * 更新档案表(物理数据)
     */
    @RequestMapping(value = "/updateArchive", method = RequestMethod.POST)
    @ApiOperation(value = "更新档案表", notes = "hkt")
    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult updateArchive(UserArchive userArchive) {

        return staffArchiveService.updateArchive(userArchive);
    }
    /**
     * 更新档案表(自定义表数据)
     */
    @RequestMapping(value = "/updateArchiveField ", method = RequestMethod.GET)
    @ApiOperation(value = "更新档案表(自定义表数据)", notes = "hkt")
    @ApiImplicitParam(name = "map", value = "字段id与对应的字段名", paramType = "form",  required = true)
    public ResponseResult updateArchiveField(Map<Integer,String> map){
        return staffArchiveService.updateArchiveField(map);
    }

    /**
     * 查看档案(查询单个)
     */
    @RequestMapping(value = "/selectArchive", method = RequestMethod.POST)
    @ApiOperation(value = "查看档案", notes = "hkt")
    public ResponseResult selectArchive() {
        Integer archiveId = getUserSession().getArchiveId();
        return staffArchiveService.selectArchive(archiveId);
    }

    /**
     * 查看档案（查询某个组织部门下的档案）
     */
    @RequestMapping(value = "/selectArchivebatch", method = RequestMethod.POST)
    @ApiOperation(value = "查看档案（查询某个组织部门下的档案）", notes = "hkt")
    @ApiImplicitParam(name = "Integer", value = "页面的机构comanyId", paramType = "query", required = true)
    public ResponseResult<PageResult<UserArchive>> selectArchivebatch(Integer comanyId) {
        Integer archiveId = getUserSession().getArchiveId();
        return staffArchiveService.selectArchivebatch(comanyId,archiveId);
    }

    /**
     * 新增人员岗位关系，初期只涉及到是否兼职，兼职是人员岗位关系中任职类型的一种
     */

    @RequestMapping(value = "/insertUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "新增人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult insertUserArchivePostRelation(UserArchivePostRelationVo userArchivePostRelationVo) {
        Integer archiveId = userSession.getArchiveId();
        return staffArchiveService.insertUserArchivePostRelation(userArchivePostRelationVo,archiveId);
    }

    /**
     * 删除人员岗位关系，初期只涉及到是否兼职
     */

    @RequestMapping(value = "/deleteUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "删除人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "人员档案关系表id集合", paramType = "query", required = true)
    public ResponseResult deleteUserArchivePostRelation(List<Integer> list) {
        return staffArchiveService.deleteUserArchivePostRelation(list);
    }

    /**
     * 修改人员岗位关系，初期值涉及到是否兼职
     */

    @RequestMapping(value = "/updateUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "修改人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult updateUserArchivePostRelation( UserArchivePostRelation userArchivePostRelation) {

        return staffArchiveService.updateUserArchivePostRelation(userArchivePostRelation);
    }

    /**
     * 展示人员岗位关系，初期只涉及到是否兼职
     */

    @RequestMapping(value = "/selectUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "展示人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
            @ApiImplicitParam(name = "list", value = "员工档案id集合", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<UserArchivePostRelation>> selectUserArchivePostRelation(Integer currentPage,
                                                                                             Integer pageSize,
                                                                                             List<Integer> list) {
        return staffArchiveService.selectUserArchivePostRelation(currentPage,pageSize,list);
    }
    /**
     * 通过id查询到对应机构名称
     */
    @RequestMapping(value = "/selectOrgName", method = RequestMethod.GET)
    @ApiOperation(value = "通过id查询到对应机构名称", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "机构id", paramType = "query", required = true)

    public ResponseResult selectOrgName(Integer id) {
        return staffArchiveService.selectOrgName(id);
    }
    /**
     * 保存修改方案
     * 包括新增与更新
     */
    @RequestMapping(value = "/saveQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "baocun查询方案", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "QueryScheme", value = "查询方案", paramType = "form", required = true),
            @ApiImplicitParam  (name = "querySchemeFieldlist", value = "查询字段", paramType = "form", required = true),
            @ApiImplicitParam (name = "querySchemeSortlist", value = "查询方案", paramType = "form", required = true),

    })
    public ResponseResult saveQueryScheme(QueryScheme queryScheme, List<QuerySchemeField> querySchemeFieldlist,
                                          List<QuerySchemeSort> querySchemeSortlist) {
        return staffArchiveService.saveQueryScheme(queryScheme,querySchemeFieldlist,querySchemeSortlist);
    }


    /**
     * 删除查询方案
     */

    @RequestMapping(value = "/deleteQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "删除查询方案", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "查询方案id的集合", paramType = "query", required = true)
    public ResponseResult deleteQueryScheme(List<Integer> list) {
        return staffArchiveService.deleteQueryScheme(list);
    }


    /**
     * 展示查询方案
     * 根据排序字段确认顺序，根据查询方案id找到显示字段与排序字段
     */
    @RequestMapping(value = "/selectQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "展示查询方案", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "查询方案id", paramType = "query", required = true)
    public ResponseResult<QuerySchemeList> selectUserArchivePostRelation(Integer id) {
        return staffArchiveService.selectQueryScheme(id);
    }
    /**
     * 通过id找到人员姓名与工号
     */
    @RequestMapping(value = "/selectNameAndNumber", method = RequestMethod.GET)
    @ApiOperation(value = "通过id找到人员姓名与工号", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<Map<String,String>> selectNameAndNumber(Integer id) {
        return staffArchiveService.selectNameAndNumber(id);
    }


    //TODO 人员进行查询时，需在后面加上机构，组织或者岗位id


}
