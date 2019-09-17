package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffcon")
@Api(tags = "【人员管理】合同相关接口")
public class StaffContractController {
    /**
     * 展示未签合同
     */
    @RequestMapping(value = "/selectNoLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "展示未签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
    })
    public ResponseResult<PageResult<List<LaborContract>>> selectLaborContract(Integer number, Integer pageSize) {
        PageResult<List<LaborContract>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<LaborContract>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 新签单个合同（合同编号支持输入，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     */

    @RequestMapping(value = "/insertLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "新签合同", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "合同表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertLaborContract(LaborContract laborContract) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 批量新签合同（合同编号不支持输入，需系统自动生成，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     */

    @RequestMapping(value = "/insertLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量新签合同", notes = "hkt")
    @ApiImplicitParam(name = "LaborContract", value = "合同表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertLaborContractBatch(LaborContract laborContract) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 保存合同（对应的是保存，就是合同已经编辑，此时字段是否已签为未签）
     */

    @RequestMapping(value = "/saveLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "保存合同", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "合同表", paramType = "form", required = true)
    public ResponseResult<Boolean> SavetLaborContract(LaborContract laborContract) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 删除合同
     */


    @RequestMapping(value = "/deleteLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "删除合同", notes = "hkt")
    @ApiImplicitParam(name = "LaborContractid", value = "合同id", paramType = "query", required = true)
    public ResponseResult<Boolean> deleteLaborContract(Integer id) {
        //真删除
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 合同变更
     * 修改合同表中的数据
     * 添加变更记录
     */
    @RequestMapping(value = "/updatelaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "修改合同内容同时添加变更记录", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContract", value = "页面信息组合成的合同对象", paramType = "form", required = true),
            @ApiImplicitParam(name = "laborContractChange", value = "页面信息组合成的合同变更对象", paramType = "query", required = true)
    })
    public ResponseResult<Boolean> updateCustomTableData(Integer id, LaborContract laborContract, LaborContractChange laborContractChange) {
        ResponseResult<Boolean> updateResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return updateResponseResult;
    }

    /**
     * 展示合同的变更记录，对合同的状态进行变更的，都应该有变更记录
     */

    @RequestMapping(value = "/selectLaborContractchange", method = RequestMethod.GET)
    @ApiOperation(value = "展示合同的变更记录", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "合同id", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
    })
    public ResponseResult<PageResult<List<LaborContractChange>>> selectLaborContractchange(Integer number, Integer pageSize) {
        PageResult<List<LaborContractChange>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<LaborContractChange>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 合同续签
     * 续签有自己的页面，这个续签是合同的一种状态，统一放在合同表中维护,删除也可以根据合同id来删除
     */

    @RequestMapping(value = "/insertReNewLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "续签签合同", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "合同表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertReNewLaborContract(LaborContract laborContract) {
        //续签时候将续签次数在原有基础上加一
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 发送续签意向表
     * 实现思路：在数据库为主一张意向书表，设置好表里面需要展示的信息。将id放进消息队列中，前端通过队列去取信息。然后展示。
     */
    @RequestMapping(value = "/insertLaborContractIntention", method = RequestMethod.GET)
    @ApiOperation(value = "发送续签意向表", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "续签意向表id", paramType = "form", required = true)
    public ResponseResult<Boolean> insertLaborContractIntention(LaborContract laborContract) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 续签反馈（表字段设计：档案id,主键id，续签意见，等）
     */
    @RequestMapping(value = "/insertResponseLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "续签反馈，数据库还未涉及这张表，用LaborContract暂时代替", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "续签反馈表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertResponseLaborContract(LaborContract laborContract) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 机构对应合同信息
     */

    @RequestMapping(value = "/selectLaborContractFromGroup", method = RequestMethod.POST)
    @ApiOperation(value = "机构对应合同信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
    })
    public ResponseResult<PageResult<List<LaborContract>>> selectLaborContractFromGroup(Integer id, Integer number, Integer pageSize) {
        PageResult<List<LaborContract>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<LaborContract>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

/**合同状态  新签、续签、解除、终止
 *  合同标识  有效、无效
 *   审批状态  未提交、审批中、已审批
 *   导入导出接口可以复用导入导出模块
 *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
 */


}
