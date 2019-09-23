package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.vo.staff.StandingBookInfo;
import com.qinjee.masterdata.service.staff.IStaffStandingBookService;
import com.qinjee.model.response.CommonCode;
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

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffsta")
@Api(tags = "【人员管理】员工台账相关接口")
public class StaffStandingBookController {
    @Autowired
    private IStaffStandingBookService staffStandingBookService;

    /**
     * 加入黑名单表
     */
    @RequestMapping(value = "/insertBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "加入黑名单表", notes = "hkt")
    @ApiImplicitParam(name = "blackListGroup", value = "黑名单表集合", paramType = "query", required = true)
    public ResponseResult insertBlackList(List<Blacklist> blacklists) {
        return staffStandingBookService.insertBlackList(blacklists);
    }

    /**
     * 删除黑名单表
     */
    @RequestMapping(value = "/deleteBalckList", method = RequestMethod.GET)
    @ApiOperation(value = "删除黑名单表", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "黑名单表id集合", paramType = "query", required = true, example = "{1，2}")
    public ResponseResult deleteBalckList(List<Integer> list) {
        return staffStandingBookService.deleteBlackList(list);
    }

    /**
     * 修改黑名单表
     */
    @RequestMapping(value = "/updateBalckList", method = RequestMethod.GET)
    @ApiOperation(value = "修改黑名单表", notes = "hkt")
    @ApiImplicitParam(name = "BlackLsit", value = "黑名单", paramType = "form", required = true)

    public ResponseResult updateBalckList(Blacklist blacklist) {
        return staffStandingBookService.updateBalckList(blacklist);
    }

    /**
     * 展示黑名单表
     */
    @RequestMapping(value = "/selectBalckList", method = RequestMethod.GET)
    @ApiOperation(value = "展示黑名单表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true)
    })
    public ResponseResult<PageResult<Blacklist>> selectBalckList(Integer currentPage, Integer pageSize) {
        return staffStandingBookService.selectBalckList(currentPage, pageSize);
    }

    /**
     * 删除台账
     */

    @RequestMapping(value = "/deleteStandingBook", method = RequestMethod.GET)
    @ApiOperation(value = "删除台账", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "台账id", paramType = "query", required = true, example = "1")
    public ResponseResult deleteStandingBook(Integer standingBookId) {
        return staffStandingBookService.deleteStandingBook(standingBookId);
    }

    /**
     * 修改台账
     */

    @RequestMapping(value = "/updateStandingBook", method = RequestMethod.GET)
    @ApiOperation(value = "修改台账", notes = "hkt")
    @ApiImplicitParam(name = "StandingBookInfo", value = "台账表信息", paramType = "form", required = true)

    public ResponseResult updateStandingBook(StandingBookInfo standingBookInfo) {
        //因为页面只有一个保存按钮，所以点保存的时候，需要区分前端是否传过来id
        //如果有id，证明是更新，做两个更新操作
        //如果没有，证明是新增。做两个新增操作。
        return staffStandingBookService.saveStandingBook(standingBookInfo);
    }

    /**
     * 查询台账
     */

    @RequestMapping(value = "/selectStandingBook", method = RequestMethod.GET)
    @ApiOperation(value = "查询台账", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "台账id", paramType = "query", required = true, example = "1")
    public ResponseResult selectStandingBook(Integer id) {
        return staffStandingBookService.selectStandingBook(id);
    }

    /**
     * 查询台账操作
     */
    @RequestMapping(value = "/selectStaff", method = RequestMethod.GET)
    @ApiOperation(value = "通过台账查询", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "IsShare", value = "是否共享", paramType = "query", required = true),
            @ApiImplicitParam(name = "StandingBookId", value = "员工台账id", paramType = "query", required = true),
            @ApiImplicitParam(name = "List<CustomFieldId>", value = "自定义字段id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "OrganztionId", value = "机构编码", paramType = "code", required = true)
    })
    public ResponseResult updateBalckList(Boolean IsShare, Integer id, List<Integer> list, String code) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        //TODO 查询台账操作与档案展示方案
        return responseResult;
    }


}
