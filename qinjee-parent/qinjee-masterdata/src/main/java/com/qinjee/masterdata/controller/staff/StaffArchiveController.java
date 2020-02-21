package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.service.staff.impl.StaffArchiveServiceImpl;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】档案相关接口")
public class StaffArchiveController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StaffArchiveController.class);
    @Autowired
    private IStaffArchiveService staffArchiveService;

    @Autowired
    private StaffArchiveServiceImpl archiveService;

    /**
     * 新增档案表
     */
    @RequestMapping(value = "/insertArchive", method = RequestMethod.POST)
    @ApiOperation(value = "新增档案表", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult<List<Integer>> insertArchive(@RequestBody @Valid UserArchiveVo userArchiveVo) throws Exception {
        Boolean b = checkParam(userArchiveVo,getUserSession());
        if(b){
            List<Integer> integer = staffArchiveService.insertArchive ( userArchiveVo, getUserSession () );
            return new ResponseResult<> ( integer,CommonCode.SUCCESS );
        }
        return  failResponseResult("档案表参数错误");
    }

    /**
     * 批量删除档案
     */
    @RequestMapping(value = "/deleteArchiveById", method = RequestMethod.POST)
    @ApiOperation(value = "删除档案", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "人员档案id集合", paramType = "query", required = true)
    public ResponseResult deleteArchiveById(@RequestBody List<Integer> archiveid) throws Exception {
        Boolean b = checkParam(archiveid);
        if(b){
                staffArchiveService.deleteArchiveById(archiveid);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("档案表id错误");
    }
    /**
     *展示部门下已经删除的档案
     */
    @RequestMapping(value = "/selectArchiveDelete", method = RequestMethod.GET)
    @ApiOperation(value = "展示部门下已经删除的档案", notes = "hkt")
    public ResponseResult<PageResult<UserArchiveVo>> selectArchiveDelete(@RequestParam List<Integer> orgId,
                                                                         @RequestParam Integer pageSize,
                                                                         @RequestParam Integer currentPage) {
        Boolean b = checkParam(orgId,pageSize,currentPage);
        if(b){
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchiveDelete ( orgId,pageSize,currentPage );
                    return new ResponseResult <> ( userArchiveVoPageResult, CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 删除恢复
     */
    @RequestMapping(value = "/resumeDeleteArchiveById", method = RequestMethod.POST)
    @ApiOperation(value = "恢复删除档案", notes = "hkt")
//    @ApiImplicitParam(name = "Archiveid", value = "人员档案id", paramType = "query", required = true)
    public ResponseResult resumeDeleteArchiveById(@RequestBody  List<Integer> archiveid) throws Exception {
        Boolean b = checkParam(archiveid);
        if(b){
                staffArchiveService.resumeDeleteArchiveById(archiveid);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("档案表id错误");
    }

    /**
     * 更新档案表(物理数据)
     */
    @RequestMapping(value = "/updateArchive", method = RequestMethod.POST)
    @ApiOperation(value = "更新档案表", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult updateArchive(@RequestBody @Valid UserArchiveVo userArchiveVo) throws Exception {
        Boolean b = checkParam(userArchiveVo,getUserSession());
        if(b){
                staffArchiveService.updateArchive(userArchiveVo,getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("档案表id错误");
    }

    /**
     * 更新档案表(自定义表数据)
     */
    @RequestMapping(value = "/updateArchiveField", method = RequestMethod.POST )
    @ApiOperation(value = "更新档案表(自定义表数据)", notes = "hkt")
//    @ApiImplicitParam(name = "map", value = "字段id与对应的字段名", paramType = "query",  required = true,allowMultiple = true)
    public ResponseResult updateArchiveField(@RequestBody  Map<Integer,String> map){
        Boolean b = checkParam(map);
        if(b){
                staffArchiveService.updateArchiveField(map);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("字段id与对应的字段名错误");

    }

    /**
     * 查看档案(查询当前登陆人的档案)
     */
    @RequestMapping(value = "/selectArchive", method = RequestMethod.POST)
    @ApiOperation(value = "查看档案(查询当前登陆人的档案)", notes = "hkt")
    public ResponseResult<UserArchiveVoAndHeader> selectArchiveAtOnce(Integer querySchemaId) {
        Boolean b = checkParam(getUserSession());
        if(b){
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchive ( getUserSession () );
                userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
                userArchiveVoAndHeader.setHeads ( archiveService.setDefaultHead ( getUserSession (), querySchemaId ) );
                return new ResponseResult<>(userArchiveVoAndHeader,CommonCode.SUCCESS);

        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 查看档案(查询单个)
     */
    @RequestMapping(value = "/selectArchiveSingle", method = RequestMethod.GET)
    @ApiOperation(value = "查看档案", notes = "hkt")
    public ResponseResult<UserArchiveVoAndHeader> selectArchiveSingle(Integer id,Integer querySchemaId) {
        Boolean b = checkParam(id,querySchemaId);
        if(b){
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchiveSingle ( id, getUserSession () );
                userArchiveVoAndHeader.setPageResult (userArchiveVoPageResult  );
                userArchiveVoAndHeader.setHeads ( archiveService.setDefaultHead ( getUserSession (), querySchemaId ) );
                return new ResponseResult<>(userArchiveVoAndHeader,CommonCode.SUCCESS);

        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 根据id查询人员信息
     */
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @ApiOperation(value = "根据id查询人员信息", notes = "hkt")
    public ResponseResult<UserArchiveVo> selectArchiveSingle(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            UserArchiveVo userArchiveVo = staffArchiveService.selectById ( id );
            return new ResponseResult <> ( userArchiveVo,CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 通过id找到人员姓名与工号
     */
    @RequestMapping(value = "/selectNameAndNumber", method = RequestMethod.GET)
    @ApiOperation(value = "通过id找到人员姓名与工号", notes = "hkt")
    public ResponseResult<Map<String,String>> selectNameAndNumber(Integer id) {
        Boolean b = checkParam(id);
        if(b){
                Map<String, String> stringStringMap = staffArchiveService.selectNameAndNumber(id);
                return new ResponseResult<>(stringStringMap,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 查看档案（查询某个组织部门下的档案）
     */
    @RequestMapping(value = "/selectArchivebatch", method = RequestMethod.POST)
    @ApiOperation(value = "查看档案（查询某个组织部门下的档案）", notes = "hkt")
    public ResponseResult<UserArchiveVoAndHeader>  selectArchivebatch(@RequestBody RequestUserarchiveVo requestUserarchiveVo) {
        Boolean b = checkParam(requestUserarchiveVo);
        if(b){
            UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
            PageResult < UserArchiveVo > userArchiveVoPageResult;
            userArchiveVoPageResult = staffArchiveService.selectArchivebatch ( getUserSession (), requestUserarchiveVo.getOrgId (),
                       requestUserarchiveVo.getPageSize (), requestUserarchiveVo.getCurrentPage () );
                userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
                userArchiveVoAndHeader.setHeads ( archiveService.setDefaultHead ( getUserSession (), requestUserarchiveVo.getQuerySchemaId() ) );
                return new ResponseResult<>(userArchiveVoAndHeader, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_SESSION);
    }

    /**
     * 新增人员岗位关系，初期只涉及到是否兼职，兼职是人员岗位关系中任职类型的一种
     */

    @RequestMapping(value = "/insertUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "新增人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    public ResponseResult insertUserArchivePostRelation(@RequestBody @Valid UserArchivePostRelationVo userArchivePostRelationVo) throws ParseException {
        Boolean b = checkParam(getUserSession());
        if(b){
                staffArchiveService.insertUserArchivePostRelation(userArchivePostRelationVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("session错误");
    }

    /**
     * 删除人员岗位关系，初期只涉及到是否兼职
     */
    @RequestMapping(value = "/deleteUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "删除人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
    public ResponseResult deleteUserArchivePostRelation(@RequestBody List<Integer> list) throws Exception {
        Boolean b = checkParam(list);
        if(b){
                staffArchiveService.deleteUserArchivePostRelation(list);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("list参数错误");
    }

    /**
     * 修改人员岗位关系，初期值涉及到是否兼职
     */
    @RequestMapping(value = "/updateUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "修改人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult updateUserArchivePostRelation(@RequestBody @Valid UserArchivePostRelationVo userArchivePostRelationVo) throws ParseException {
        Boolean b = checkParam(userArchivePostRelationVo,getUserSession ());
        if(b){
                staffArchiveService.updateUserArchivePostRelation(userArchivePostRelationVo,getUserSession ());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("userArchivePostRelation参数不合法");
    }

    /**
     * 展示人员岗位关系，初期只涉及到是否兼职
     */
    @RequestMapping(value = "/selectUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "展示人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
//            @ApiImplicitParam(name = "list", value = "员工档案id集合", paramType = "query", required = true)
//    })
    public ResponseResult selectUserArchivePostRelation(@RequestParam Integer archiveId) {
        Boolean b = checkParam(archiveId);
        if(b){
                List < UserArchivePostRelationVo > list = staffArchiveService.selectUserArchivePostRelation ( archiveId );
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 通过id查询到对应机构名称
     */
    @RequestMapping(value = "/selectOrgName", method = RequestMethod.GET)
    @ApiOperation(value = "通过id查询到对应机构名称", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "机构id", paramType = "query", required = true)

    public ResponseResult selectOrgName(Integer id) {
        Boolean b = checkParam(id,getUserSession ());
        if(b){
                String s = staffArchiveService.selectOrgName(id,getUserSession ());
                return new ResponseResult<>(s,CommonCode.SUCCESS);
        }
        return  failResponseResult("id错误");
    }

    /**
     * 查看权限下的对应表的显示字段
     */
    @RequestMapping(value = "/selectFieldByTableIdAndAuth", method = RequestMethod.GET)
    @ApiOperation(value = "查看权限下的对应表的显示字段", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "tableId", paramType = "query", required = true)

    public ResponseResult<List<String>> selectFieldByTableIdAndAuth(Integer tableId) {
        Boolean b = checkParam(tableId,getUserSession());
        if(b){
                List<String> list= staffArchiveService.selectFieldByTableIdAndAuth(tableId,getUserSession());
                return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
       return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 查看权限下的档案表的显示字段
     */
    @RequestMapping(value = "/selectFieldByArcAndAuth", method = RequestMethod.POST)
    @ApiOperation(value = "查看权限下的档案表的显示字段", notes = "hkt")
    public ResponseResult<List<String>> selectFieldByArcAndAuth() {
        Boolean b = checkParam(getUserSession());
        if(b){
                List<String> list= staffArchiveService.selectFieldByArcAndAuth(getUserSession());
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 保存修改方案
     * 包括新增与更新
     */
    @RequestMapping(value = "/saveQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "保存查询方案", notes = "hkt")
    public ResponseResult saveQueryScheme(@RequestBody @Valid QuerySchemaVo querySchemaVo){
        Boolean b = checkParam(querySchemaVo);
        if(b){
                staffArchiveService.saveQueryScheme(querySchemaVo);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     *
     设置默认查询方案
     */

    @RequestMapping(value = "/setDefaultQuerySchme", method = RequestMethod.GET)
    @ApiOperation(value = "设置默认方案", notes = "hkt")
    public ResponseResult setDefaultQuerySchme(Integer querySchmeId){
        Boolean b = checkParam(querySchmeId,getUserSession ());
        if(b){
                staffArchiveService.setDefaultQuerySchme(querySchmeId,getUserSession ());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数为空");
    }

    /**
     * 删除查询方案
     */
    @RequestMapping(value = "/deleteQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "删除查询方案", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "查询方案id的集合", paramType = "query", required = true)
    public ResponseResult deleteQueryScheme(@RequestBody List<Integer> list) throws Exception {
        Boolean b = checkParam(list);
        if(b){
                staffArchiveService.deleteQueryScheme(list);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("list错误");

    }


    /**
     * 展示查询方案
     * 根据排序字段确认顺序，根据查询方案id找到显示字段与排序字段
     */
    @RequestMapping(value = "/selectQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "展示查询方案", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "查询方案id", paramType = "query", required = true)
    public ResponseResult<List<QueryScheme>> selectUserArchiveQuerySchema() {
        Boolean b = checkParam(getUserSession ());
        if(b){
                List < QueryScheme > lists = staffArchiveService.selectQueryScheme(getUserSession ());
                    return new ResponseResult<>(lists,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 展示查询方案信息
     */
    @RequestMapping(value = "/selectQuerySchemeMessage", method = RequestMethod.POST)
    @ApiOperation(value = "展示查询方案信息", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "查询方案id", paramType = "query", required = true)
    public ResponseResult<QuerySchemeList> selectUserArchiveQuery(Integer id) {
        Boolean b = checkParam();
        if(b){
                QuerySchemeList querySchemeList = staffArchiveService.selectQuerySchemeMessage ( id );
                    return new ResponseResult<>(querySchemeList,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 根据显示方案展示人员信息
     */
    @RequestMapping(value = "/selectArchiveByQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "根据显示方案展示人员信息", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "schemeId", value = "查询方案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "orgId", value = "机构id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true)
//    })
    public ResponseResult< ExportFile > selectArchiveByQueryScheme(@RequestBody List<Integer> archiveIdList, Integer queryschemaId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Boolean b = checkParam(getUserSession(),archiveIdList);
        if(b){
                ExportFile exportFile =
                        staffArchiveService.selectArchiveByQueryScheme( getUserSession(), archiveIdList,queryschemaId);
                    return new ResponseResult<>(exportFile,CommonCode.SUCCESS);

        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 新增员工轨迹
     */
    @RequestMapping(value = "/insertCareerTrack", method = RequestMethod.POST)
    @ApiOperation(value = "新增员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult selectCareerTrack(@RequestBody @Valid ArchiveCareerTrackVo archiveCareerTrackVo) throws IllegalAccessException {
        Boolean b = checkParam(archiveCareerTrackVo,getUserSession());
        if(b){
                staffArchiveService.insertCareerTrack(archiveCareerTrackVo,getUserSession());
                return  ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 展示员工轨迹
     */
    @RequestMapping(value = "/selectCareerTrack", method = RequestMethod.POST)
    @ApiOperation(value = "展示员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<List<ArchiveCareerTrackVo>> selectCareerTrack(Integer id) {
        Boolean b = checkParam(id);
        if(b){
                List<ArchiveCareerTrackVo> list=staffArchiveService.selectCareerTrack(id);
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }



    /**
     * 根据姓名返回UserArchive
     */
    @RequestMapping(value = "/selectUserArchiveByName", method = RequestMethod.GET)
    @ApiOperation(value = "根据姓名返回UserArchive", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<List<UserArchiveVo>> selectUserArchiveByName(String name) {
        Boolean b = checkParam(name,getUserSession ());
        if(b){
                List<UserArchiveVo> userArchiveList=staffArchiveService. selectUserArchiveByName(name,getUserSession ());
               return new ResponseResult <> ( userArchiveList,CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }



    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if(param instanceof UserSession){
                if(null == param|| "".equals(param)){
                    ExceptionCast.cast ( CommonCode.INVALID_SESSION );
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }


    private ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }

}
