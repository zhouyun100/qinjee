package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.StandingBook;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.BlackListVo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfoVo;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffsta")
@Api(tags = "【人员管理】员工台账相关接口")
public class StaffStandingBookController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StaffStandingBookController.class);
    @Autowired
    private IStaffStandingBookService staffStandingBookService;

    /**
     * 加入黑名单表
     */
    @RequestMapping(value = "/insertBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "加入黑名单表", notes = "hkt")
//    @ApiImplicitParam(name = "blackListGroup", value = "黑名单表集合", paramType = "query", required = true)
    public ResponseResult insertBlackList(@RequestBody List<BlackListVo> blacklists, String dataSource) {
        Boolean b = checkParam(blacklists, dataSource, getUserSession());
        if (b) {
            try {
                staffStandingBookService.insertBlackList(blacklists, dataSource, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("加入黑名单表失败");
            }

        }
        return failResponseResult("参数错误");
    }
    /**
     * 删除黑名单表
     */
    @RequestMapping(value = "/deleteBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "删除黑名单表", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "黑名单表id集合", paramType = "query", required = true, example = "{1，2}")
    public ResponseResult deleteBalckList(@RequestBody List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffStandingBookService.deleteBlackList(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("删除黑名单表失败");
            }
        }
        return failResponseResult("参数错误");
    }

    /**
     * 修改黑名单表
     */
    @RequestMapping(value = "/updateBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "修改黑名单表", notes = "hkt")
//    @ApiImplicitParam(name = "BlackLsit", value = "黑名单", paramType = "form", required = true)

    public ResponseResult updateBalckList(@RequestBody @Valid Blacklist blacklist) {
        Boolean b = checkParam(blacklist);
        if (b) {
            try {
                staffStandingBookService.updateBalckList(blacklist);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改黑名单表失败");
            }
        }
        return failResponseResult("参数错误");
    }

    /**
     * 展示黑名单表
     */
    @RequestMapping(value = "/selectBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "展示黑名单表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true)
//    })
    public ResponseResult<PageResult<Blacklist>> selectBalckList(Integer currentPage, Integer pageSize) {
        Boolean b = checkParam(currentPage,pageSize);
        if (b) {
            try {
                PageResult<Blacklist> pageResult =
                        staffStandingBookService.selectBalckList(currentPage, pageSize);
                if(pageResult.getList().size()>0){
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                    return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 删除台账
     */

    @RequestMapping(value = "/deleteStandingBook", method = RequestMethod.POST)
    @ApiOperation(value = "删除台账", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "台账id", paramType = "query", required = true, example = "1")
    public ResponseResult deleteStandingBook(Integer standingBookId) {
        Boolean b = checkParam(standingBookId);
        if (b) {
            try {
                staffStandingBookService.deleteStandingBook(standingBookId);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("删除台账失败");
            }
        }
        return failResponseResult("参数错误");
    }

    /**
     * 新增与修改台账
     */

    @RequestMapping(value = "/updateStandingBook", method = RequestMethod.POST)
    @ApiOperation(value = "修改台账", notes = "hkt")
//    @ApiImplicitParam(name = "StandingBookInfoVo", value = "台账表信息", paramType = "form", required = true)

    public ResponseResult saveStandingBook(@Valid @RequestBody StandingBookInfoVo standingBookInfoVo) {
        //因为页面只有一个保存按钮，所以点保存的时候，需要区分前端是否传过来id
        //如果有id，证明是更新，做两个更新操作
        //如果没有，证明是新增。做两个新增操作。
        Boolean b = checkParam(standingBookInfoVo,getUserSession());
        if (b) {
            try {
                staffStandingBookService.saveStandingBook(getUserSession(), standingBookInfoVo);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("查询台账失败");
            }
        }
        return failResponseResult("参数错误");
    }

    /**
     * 查询台账
     */

    @RequestMapping(value = "/selectStandingBook", method = RequestMethod.POST)
    @ApiOperation(value = "查询台账", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "台账id", paramType = "query", required = true, example = "1")
    public ResponseResult<StandingBookInfo> selectStandingBook(Integer id) {
        Boolean b = checkParam(id);
        if (b) {
            try {
                StandingBookInfo standingBookInfo = staffStandingBookService.selectStandingBook(id);
                if(standingBookInfo !=null){
                    return new ResponseResult<>(standingBookInfo,CommonCode.SUCCESS);
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
     * 查看我的台账，不含共享
     */
    @RequestMapping(value = "/selectMyStandingBook", method = RequestMethod.POST)
    @ApiOperation(value = "查看我的台账，不含共享", notes = "hkt")
    public ResponseResult<List<StandingBook>> selectMyStandingBook() {
        Boolean b = checkParam(getUserSession());
        if (b) {
            try {
                List<StandingBook> list = staffStandingBookService.selectMyStandingBook(userSession);
                if(list!=null){
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
     * 查看我的台账，含共享
     */
    @RequestMapping(value = "/selectMyStandingBookShare", method = RequestMethod.POST)
    @ApiOperation(value = "查看我的台账，含是否共享", notes = "hkt")
    public ResponseResult selectMyStandingBookShare() {
        Boolean b = checkParam(getUserSession());
        if (b) {
            try {
                List<StandingBook> list = staffStandingBookService.selectMyStandingBookShare(getUserSession());
                if(list!=null){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return failResponseResult("台账为空");
            } catch (Exception e) {
                return failResponseResult("查询台账失败");
            }
        }
        return failResponseResult("参数错误");
    }

    /**
     * 查询台账操作
     *梳理：1，寻找表名，根据企业id，找到自定义表，在前端页面展示为表名。
     *     2，寻找字段名，通过表名找到表id，通过id找到字段名。(用户在添加自定义字段时，会将中文字段名存到字段名中，
     *     将英文存入物理字段名。而后我们会将物理字段名作为key存进自定义数据表中。)
     *     3，操作符中的包含分为字符串与非字符串两种情况
     *     4，连接符分为或者与并且
     */
    @RequestMapping(value = "/selectStaff", method = RequestMethod.POST)
    @ApiOperation(value = "通过台账查询", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "StandingBookId", value = "员工台账id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "archiveType", value = "人员分类", paramType = "query", required = true),
//            @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "type", value = "兼职状态", paramType = "query", required = true),
//
//    })
    public ResponseResult<List<UserArchive>> selectStaff(Integer stangdingBookId, String archiveType, Integer orgId, String type){
        Boolean b = checkParam(stangdingBookId,archiveType,orgId,type,getUserSession());
        if (b) {
            try {
                List<UserArchive> list=staffStandingBookService.selectStaff(stangdingBookId,archiveType,orgId,type,getUserSession());
                if(list!=null){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
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
