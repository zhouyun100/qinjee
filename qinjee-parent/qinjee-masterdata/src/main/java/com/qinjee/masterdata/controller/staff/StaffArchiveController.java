package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.ArchiveCareerTrack;
import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.model.vo.staff.export.ExportFile;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    /**
     * 新增档案表
     */
    @RequestMapping(value = "/insertArchive", method = RequestMethod.POST)
    @ApiOperation(value = "新增档案表", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult insertArchive(@RequestBody @Valid UserArchiveVo userArchiveVo) {
        Boolean b = checkParam(userArchiveVo,getUserSession());
        if(b){
            try {
                staffArchiveService.insertArchive(userArchiveVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增档案表失败");
            }

        }
        return  failResponseResult("档案表参数错误");

    }

    /**
     * 批量删除档案
     */
    @RequestMapping(value = "/deleteArchiveById", method = RequestMethod.POST)
    @ApiOperation(value = "删除档案", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "人员档案id集合", paramType = "query", required = true)
    public ResponseResult deleteArchiveById(@RequestBody List<Integer> archiveid) {
        Boolean b = checkParam(archiveid);
        if(b){
            try {
                staffArchiveService.deleteArchiveById(archiveid);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除档案失败");
            }

        }
        return  failResponseResult("档案表id错误");
    }
    /**
     * 删除恢复
     */
    @RequestMapping(value = "/resumeDeleteArchiveById", method = RequestMethod.GET)
    @ApiOperation(value = "恢复删除档案", notes = "hkt")
//    @ApiImplicitParam(name = "Archiveid", value = "人员档案id", paramType = "query", required = true)
    public ResponseResult resumeDeleteArchiveById(Integer archiveid) {
        Boolean b = checkParam(archiveid);
        if(b){
            try {
                staffArchiveService.resumeDeleteArchiveById(archiveid);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("恢复删除档案失败");
            }
        }
        return  failResponseResult("档案表id错误");
    }
    /**
     * 更新档案表(物理数据)
     */
    @RequestMapping(value = "/updateArchive", method = RequestMethod.POST)
    @ApiOperation(value = "更新档案表", notes = "hkt")
    @ApiImplicitParam(name = "UserArchive", value = "人员档案", paramType = "form", required = true)
    public ResponseResult updateArchive(@RequestBody @Valid UserArchiveVo userArchiveVo) {
        Boolean b = checkParam(userArchiveVo,getUserSession());
        if(b){
            try {
                staffArchiveService.updateArchive(userArchiveVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("更新档案表失败");
            }
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
        for (Integer integer : map.keySet()) {
            System.out.println(integer);
        }
        Boolean b = checkParam(map);
        if(b){
            try {
                staffArchiveService.updateArchiveField(map);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("更新档案表（自定义字段表）失败");
            }
        }
        return  failResponseResult("字段id与对应的字段名错误");

    }

    /**
     * 查看档案(查询当前登陆人的档案)
     */
    @RequestMapping(value = "/selectArchive", method = RequestMethod.POST)
    @ApiOperation(value = "查看档案(查询当前登陆人的档案)", notes = "hkt")
    public ResponseResult<UserArchiveVoAndHeader> selectArchiveAtOnce() {
        Boolean b = checkParam(getUserSession());
        if(b){
            try {
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchive ( getUserSession () );
                userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
                userArchiveVoAndHeader.setHeads ( staffArchiveService.getHeadList ( userSession));
                return new ResponseResult<>(userArchiveVoAndHeader,CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 查看档案(查询单个)
     */
    @RequestMapping(value = "/selectArchiveSingle", method = RequestMethod.GET)
    @ApiOperation(value = "查看档案", notes = "hkt")
    public ResponseResult<UserArchiveVoAndHeader> selectArchiveSingle(Integer id ) {
        Boolean b = checkParam(id);
        if(b){
            try {
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchiveSingle ( id, getUserSession () );
                userArchiveVoAndHeader.setPageResult (userArchiveVoPageResult  );
                userArchiveVoAndHeader.setHeads ( staffArchiveService.getHeadList ( getUserSession () ) );
                return new ResponseResult<>(userArchiveVoAndHeader,CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 通过id找到人员姓名与工号
     */
    @RequestMapping(value = "/selectNameAndNumber", method = RequestMethod.GET)
    @ApiOperation(value = "通过id找到人员姓名与工号", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<Map<String,String>> selectNameAndNumber(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                Map<String, String> stringStringMap = staffArchiveService.selectNameAndNumber(id);
                return new ResponseResult<>(stringStringMap,CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 查看档案（查询某个组织部门下的档案）
     */
    @RequestMapping(value = "/selectArchivebatch", method = RequestMethod.GET)
    @ApiOperation(value = "查看档案（查询某个组织部门下的档案）", notes = "hkt")
//    @ApiImplicitParam(name = "Integer", value = "页面的机构comanyId", paramType = "query", required = true)
    public ResponseResult<UserArchiveVoAndHeader>  selectArchivebatch(Integer orgId,Integer pageSize,Integer currentPage) {
        Boolean b = checkParam(getUserSession(),orgId,pageSize,currentPage);
        if(b){
            try {
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                PageResult < UserArchiveVo > userArchiveVoPageResult = staffArchiveService.selectArchivebatch ( getUserSession (), orgId, pageSize, currentPage );
                userArchiveVoAndHeader.setPageResult ( userArchiveVoPageResult );
                userArchiveVoAndHeader.setHeads ( staffArchiveService.getHeadList ( getUserSession ()) );
                if(userArchiveVoAndHeader!=null) {
                    return new ResponseResult<>(userArchiveVoAndHeader, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace ();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_SESSION);
    }

    /**
     * 新增人员岗位关系，初期只涉及到是否兼职，兼职是人员岗位关系中任职类型的一种
     */

    @RequestMapping(value = "/insertUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "新增人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult insertUserArchivePostRelation(@RequestBody @Valid UserArchivePostRelationVo userArchivePostRelationVo) {
        Boolean b = checkParam(getUserSession());
        if(b){
            try {
                staffArchiveService.insertUserArchivePostRelation(userArchivePostRelationVo, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增人员岗位关系失败");
            }
        }
        return  failResponseResult("session错误");
    }

    /**
     * 删除人员岗位关系，初期只涉及到是否兼职
     */
    @RequestMapping(value = "/deleteUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "删除人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "人员档案关系表id集合", paramType = "query", required = true)
    public ResponseResult deleteUserArchivePostRelation(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if(b){
            try {
                staffArchiveService.deleteUserArchivePostRelation(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除人员岗位关系失败");
            }
        }
        return  failResponseResult("list参数错误");
    }

    /**
     * 修改人员岗位关系，初期值涉及到是否兼职
     */
    @RequestMapping(value = "/updateUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "修改人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult updateUserArchivePostRelation(@RequestBody @Valid UserArchivePostRelation userArchivePostRelation) {
        Boolean b = checkParam(userArchivePostRelation);
        if(b){
            try {
                staffArchiveService.updateUserArchivePostRelation(userArchivePostRelation);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改人员岗位关系，初期只涉及任职状态是否兼职失败");
            }
        }
        return  failResponseResult("userArchivePostRelation参数不合法");
    }

    /**
     * 展示人员岗位关系，初期只涉及到是否兼职
     */
    @RequestMapping(value = "/selectUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "展示人员岗位关系，初期只涉及任职状态是否兼职", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
//            @ApiImplicitParam(name = "list", value = "员工档案id集合", paramType = "query", required = true)
//    })
    public ResponseResult<PageResult<UserArchivePostRelation>> selectUserArchivePostRelation(Integer currentPage,
                                                                                             Integer pageSize,
                                                                                            @RequestBody List<Integer> list) {
        Boolean b = checkParam(currentPage,pageSize,list);
        if(b){
            try {
                PageResult<UserArchivePostRelation> pageResult =
                        staffArchiveService.selectUserArchivePostRelation(currentPage, pageSize, list);
                if(pageResult!=null) {
                    return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
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
            try {
                String s = staffArchiveService.selectOrgName(id,getUserSession ());
                return new ResponseResult<>(s,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("通过id查询到对应机构名称失败");
            }
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
            try {
                List<String> list= staffArchiveService.selectFieldByTableIdAndAuth(tableId,getUserSession());
                if(!CollectionUtils.isEmpty(list)){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return new ResponseResult(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }

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
            try {
                List<String> list= staffArchiveService.selectFieldByArcAndAuth(getUserSession());
                if(!CollectionUtils.isEmpty(list)){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return new ResponseResult(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }

        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 保存修改方案
     * 包括新增与更新
     */
    @RequestMapping(value = "/saveQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "保存查询方案", notes = "hkt")
    public ResponseResult saveQueryScheme(@RequestBody @Valid QueryArcVo queryArcVo){
        Boolean b = checkParam(queryArcVo);
        if(b){
            try {
                staffArchiveService.saveQueryScheme(queryArcVo);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("保存查询方案");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     *
     设置默认查询方案
     */

    @RequestMapping(value = "/setDefaultQuerySchme", method = RequestMethod.GET)
    @ApiOperation(value = "保存查询方案", notes = "hkt")
    public ResponseResult setDefaultQuerySchme(Integer querySchmeId){
        Boolean b = checkParam(querySchmeId,getUserSession ());
        if(b){
            try {
                staffArchiveService.setDefaultQuerySchme(querySchmeId,getUserSession ());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("设置查询方案失败");
            }
        }
        return  failResponseResult("参数为空");
    }

    /**
     * 删除查询方案
     */
    @RequestMapping(value = "/deleteQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "删除查询方案", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "查询方案id的集合", paramType = "query", required = true)
    public ResponseResult deleteQueryScheme(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if(b){
            try {
                staffArchiveService.deleteQueryScheme(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("删除查询方案失败");
            }
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
    public ResponseResult<List<QueryScheme>> selectUserArchivePostRelation() {
        Boolean b = checkParam(getUserSession ());
        if(b){
            try {
                List < QueryScheme > lists = staffArchiveService.selectQueryScheme(getUserSession ());
                if(null!=lists){
                    return new ResponseResult<>(lists,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 展示查询方案信息
     */
    @RequestMapping(value = "/selectQuerySchemeMessage", method = RequestMethod.POST)
    @ApiOperation(value = "展示查询方案信息", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "查询方案id", paramType = "query", required = true)
    public ResponseResult<QuerySchemeList> selectUserArchivePostRelation(Integer id) {
        Boolean b = checkParam();
        if(b){
            try {
                QuerySchemeList querySchemeList = staffArchiveService.selectQuerySchemeMessage ( id );
                if(null!=querySchemeList){
                    return new ResponseResult<>(querySchemeList,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
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
    public ResponseResult< ExportFile > selectArchiveByQueryScheme(@RequestBody List<Integer> archiveIdList) {
        Boolean b = checkParam(getUserSession(),archiveIdList);
        if(b){
            try {
                ExportFile exportFile =
                        staffArchiveService.selectArchiveByQueryScheme( getUserSession(), archiveIdList);
                if(exportFile!=null){
                    return new ResponseResult<>(exportFile,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 新增员工轨迹
     */
    @RequestMapping(value = "/insertCareerTrack", method = RequestMethod.POST)
    @ApiOperation(value = "新增员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult selectCareerTrack(@RequestBody @Valid ArchiveCareerTrackVo archiveCareerTrackVo) {
        Boolean b = checkParam(archiveCareerTrackVo,getUserSession());
        if(b){
            try {
                staffArchiveService.insertCareerTrack(archiveCareerTrackVo,getUserSession());
                return  ResponseResult.SUCCESS();
            } catch (Exception e) {
                return ResponseResult.FAIL();
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 展示员工轨迹
     */
    @RequestMapping(value = "/selectCareerTrack", method = RequestMethod.POST)
    @ApiOperation(value = "展示员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<List<ArchiveCareerTrack>> selectCareerTrack(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                List<ArchiveCareerTrack> list=staffArchiveService.selectCareerTrack(id);
                if(list.size()>0){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 修改员工轨迹
     */
    @RequestMapping(value = "/updateCareerTrack", method = RequestMethod.POST)
    @ApiOperation(value = "修改员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult updateCareerTrack(@RequestBody @Valid ArchiveCareerTrackVo archiveCareerTrackVo) {
        Boolean b = checkParam(archiveCareerTrackVo,getUserSession());
        if(b){
            try {
                staffArchiveService. updateCareerTrack( archiveCareerTrackVo,getUserSession());
               return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 删除员工轨迹
     */
    @RequestMapping(value = "/deleteCareerTrack", method = RequestMethod.GET)
    @ApiOperation(value = "修改员工轨迹", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult deleteCareerTrack(Integer id) {
        //TODO 表里没有删除字段
        Boolean b = checkParam(id);
        if(b){
            try {
                staffArchiveService. deleteCareerTrack(id);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 根据姓名返回UserArchive
     * @param
     * @return
     */
    @RequestMapping(value = "/selectUserArchiveByName", method = RequestMethod.GET)
    @ApiOperation(value = "根据姓名返回UserArchive", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult<List<UserArchiveVo>> selectUserArchiveByName(String name) {
        Boolean b = checkParam(name);
        if(b){
            try {
                List<UserArchiveVo> userArchiveList=staffArchiveService. selectUserArchiveByName(name);
               return new ResponseResult <> ( userArchiveList,CommonCode.SUCCESS );
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }



    private Boolean checkParam(Object... params) {
        for (Object param : params) {
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
