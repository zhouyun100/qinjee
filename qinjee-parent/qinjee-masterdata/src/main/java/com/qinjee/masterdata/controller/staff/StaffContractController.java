package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.vo.staff.*;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.masterdata.service.staff.IStaffContractService;
import com.qinjee.model.request.UserSession;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffcon")
@Api(tags = "【人员管理】合同相关接口")
public class StaffContractController extends BaseController {
    @Autowired
    private IStaffContractService staffContractService;
    private static final Logger logger = LoggerFactory.getLogger(StaffContractController.class);

    /**
     * 展示未签合同人员分页
     */
    @RequestMapping(value = "/selectNoLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "展示未签合同人员分页", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),
//    })
    public ResponseResult< UserArchiveVoAndHeader > selectLaborContract(@RequestBody RequestUserarchiveVo requestUserarchiveVo) {
        Boolean b = checkParam(requestUserarchiveVo,getUserSession ());
        if (b) {
            PageResult < UserArchiveVo > pageResult=
                staffContractService.selectNoLaborContract (requestUserarchiveVo,userSession.getCompanyId() );
                UserArchiveVoAndHeader userArchiveVoAndHeader=new UserArchiveVoAndHeader ();
                userArchiveVoAndHeader.setPageResult (pageResult);
//                userArchiveVoAndHeader.setHeads (staffArchiveService.getHeadList(userSession));
                return new ResponseResult<>(userArchiveVoAndHeader, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**合同状态  新签、变更   续签、解除、终止
     *  合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
     *   审批状态  未提交、审批中、已审批
     *   导入导出接口可以复用导入导出模块
     *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
     */

    /**
     * 展示已签合同的人员信息分页
     */
    @RequestMapping(value = "/selectLaborContractserUser", method = RequestMethod.POST)
    @ApiOperation(value = "展示已签合同的人员信息", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),
//            @ApiImplicitParam(name = "isEnable", value = "是否有效", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "合同状态", paramType = "query", required = true),
//
//    })
    public ResponseResult<PageResult<ContractWithArchiveVo>> selectLaborContractserUser(@RequestBody RequestUserarchiveVo requestUserarchiveVo) throws Exception {
        Boolean b = checkParam(requestUserarchiveVo,getUserSession ());
        if (b) {
                PageResult<ContractWithArchiveVo> pageResult =
                        staffContractService.selectLaborContractserUser(requestUserarchiveVo,getUserSession());
             return new ResponseResult<> ( pageResult,CommonCode.SUCCESS );
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 新签单个合同（合同编号支持输入，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     */
    @RequestMapping(value = "/insertLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "新签合同", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
//            @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
//    })
    public ResponseResult insertLaborContract(@RequestBody @Valid ContractVo contractVo) throws ParseException {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.insertLaborContract(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 根据人员ID查询合同
     */
    @RequestMapping(value = "/selectContractByArchiveId", method = RequestMethod.GET)
    @ApiOperation(value = "根据人员ID查询合同", notes = "hkt")
    public ResponseResult selectContractByArchiveId( Integer archiveId) {
        Boolean b = checkParam(archiveId);
        if(b){
                List< LaborContract> list=staffContractService.selectContractByArchiveId(archiveId);
                    return new ResponseResult<> ( list,CommonCode.SUCCESS );
        }
        return  failResponseResult("参数错误");
    }

    @RequestMapping(value = "/selectAboutToExpireContracts", method = RequestMethod.GET)
    @ApiOperation(value = "根据部门id展示合同即将到期的人员信息", notes = "phs")
    public ResponseResult<PageResult<ContractWithArchiveVo>> selectAboutToExpireContracts( Integer orgId, Integer currentPage,
                                                                                           Integer pageSize) throws Exception {
        Boolean b = checkParam(orgId, currentPage, pageSize, getUserSession());
        if (b) {
            PageResult<ContractWithArchiveVo> pageResult =
                    staffContractService.selectAboutToExpireContracts(orgId, getUserSession().getArchiveId(),getUserSession().getCompanyId(),currentPage, pageSize);
            return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**id
     * 批量新签合同（合同编号不支持输入，需系统自动生成，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     * 这里需要进行人员信息的查询，然后将属性取出来塞进合同集合中。
     */
    @RequestMapping(value = "/insertLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量新签合同", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractVo", value = "合同vo表", paramType = "form", required = true),
//            @ApiImplicitParam(name = "list", value = "档案id集合", paramType = "query", required = true)
//    })
    public ResponseResult insertLaborContractBatch(@RequestBody @Valid ContractVo contractVo) throws Exception {
        Boolean b = checkParam(contractVo, getUserSession());
        if (b) {
            staffContractService.insertLaborContractBatch(contractVo, getUserSession());
            return ResponseResult.SUCCESS();
        }
        return failResponseResult("参数错误");
    }

    /**
     * 保存合同（对应的是保存，就是合同已经编辑，此时字段是否已签为未签）
     * 这个跟新增合同一样的，只是合同状态不同
     */
    @RequestMapping(value = "/SaveLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "保存合同", notes = "hkt")
    public ResponseResult saveLaborContract(@RequestBody @Valid ContractVo contractVo) throws Exception {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.saveLaborContract(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 删除合同
     */
    @RequestMapping(value = "/deleteLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "删除合同", notes = "hkt")
//    @ApiImplicitParam(name = "LaborContractid", value = "合同id", paramType = "query", required = true)
    public ResponseResult deleteLaborContract(Integer laborContractid) {
        Boolean b = checkParam(laborContractid);
        if(b){
                staffContractService.deleteLaborContract(laborContractid);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 合同变更
     * 修改合同表中的数据
     * 添加变更记录
     */
    @RequestMapping(value = "/updatelaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "修改合同内容同时添加变更记录", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContract", value = "合同信息", paramType = "form", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult updatelaborContract(@RequestBody @Valid ContractVo contractVo) throws ParseException {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.updatelaborContract(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 合同续签
     * 续签有自己的页面，这个续签是合同的一种状态，统一放在合同表中维护,删除也可以根据合同id来删除
     */

    @RequestMapping(value = "/insertReNewLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "续签合同", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
//            @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult insertReNewLaborContract(@RequestBody @Valid ContractVo contractVo) {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.insertReNewLaborContract(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 批量续签
     */
    @RequestMapping(value = "/insertReNewLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量续签合同", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
//            @ApiImplicitParam(name = "list", value = "档案id集合", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult insertReNewLaborContractBatch(@RequestBody @Valid ContractVo contractVo) throws Exception {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.insertReNewLaborContractBatch(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 获得签订次数
     */
    @RequestMapping(value = "/getSignNumber", method = RequestMethod.GET)
    @ApiOperation(value = "获得签订次数", notes = "hkt")
    public ResponseResult getSignNumber(Integer archiveId) {
        Boolean b = checkParam(archiveId);
        if(b){
                Integer signNumber = staffContractService.getSignNumber ( archiveId );
                return new ResponseResult<> ( signNumber,CommonCode.SUCCESS );
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 终止合同信息
     */
    @RequestMapping(value = "/endlaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "终止合同信息", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult endlaborContract(@RequestBody @Valid LaborContractChangeVo laborContractChangeVo, Integer id) throws ParseException {
        Boolean b = checkParam(laborContractChangeVo,id,getUserSession());
        if(b){
                staffContractService.endlaborContract(laborContractChangeVo, id,getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 批量终止合同
     */
    @RequestMapping(value = "/endlaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量终止合同", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "list", value = "合同id集合", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult endlaborContract(@RequestBody @Valid ContractVo contractVo) throws ParseException {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.endlaborContractBatch(contractVo, getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 解除合同信息
     */
    @RequestMapping(value = "/looselaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "解除合同信息", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult looselaborContract(@RequestBody @Valid ContractVo contractVo) throws ParseException {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.looselaborContract(contractVo.getLaborContractChangeVo (),contractVo.getList ().get ( 0 ), getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 批量解除合同信息
     */
    @RequestMapping(value = "/looselaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量解除合同信息", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "list", value = "合同id集合", paramType = "query", required = true),
//            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
//    })
    public ResponseResult looselaborContractBatch(@RequestBody @Valid ContractVo contractVo) throws ParseException {
        Boolean b = checkParam(contractVo,getUserSession());
        if(b){
                staffContractService.looselaborContractBatch(contractVo, getUserSession());
                return ResponseResult.SUCCESS();

        }
        return  failResponseResult("参数错误");
    }

    /**
     * 展示合同的变更记录，对合同的状态进行变更的，都应该有变更记录
     */

    @RequestMapping(value = "/selectLaborContractchange", method = RequestMethod.GET)
    @ApiOperation(value = "展示合同的变更记录", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "合同id", paramType = "query", required = true)
    public ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id) {
        Boolean b = checkParam(id);
        if(b){
                List<LaborContractChange> list = staffContractService.selectLaborContractchange(id);
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    /**
     * 发送续签意向表
     * 实现思路：拿到续签人员的档案id，根据档案中信息设置续签意向表。
     */
    @RequestMapping(value = "/insertLaborContractIntention", method = RequestMethod.GET)
    @ApiOperation(value = "发送续签意向表", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult insertLaborContractIntention(@RequestParam List<Integer> list) {
        Boolean b = checkParam(list,getUserSession());
        if(b){
                staffContractService.insertLaborContractIntention(list,getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 续签反馈
     */
    @RequestMapping(value = "/updateContractRenewalIntention", method = RequestMethod.POST)
    @ApiOperation(value = "续签反馈", notes = "hkt")
//    @ApiImplicitParam(name = "ContractRenewalIntention", value = "续签反馈表", paramType = "form", required = true)
    public ResponseResult insertResponseLaborContract(@RequestBody @Valid ContractRenewalIntention contractRenewalIntention) {
        Boolean b = checkParam(contractRenewalIntention);
        if(b){
                staffContractService.updateContractRenewalIntention(contractRenewalIntention);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");

    }
    @RequestMapping(value = "/insertMessqge", method = RequestMethod.POST)
    @ApiOperation(value = "添加续签信息", notes = "hkt")
    //    @ApiImplicitParam(name = "ContractRenewalIntention", value = "续签反馈表", paramType = "form", required = true)
    public ResponseResult insertMessqge(@RequestBody @Valid InsertRenewContractMessage insertRenewContractMessage) {
        Boolean b = checkParam(insertRenewContractMessage);
        if(b){
            staffContractService.insertMessqge(insertRenewContractMessage);
            return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");

    }
    /**
     * 展示我的续签意向
     */
    @RequestMapping(value = "/selectContractRenewalIntention", method = RequestMethod.GET)
    @ApiOperation(value = "展示我的续签意向", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "form", required = true)
    public ResponseResult<List<ContractRenewalIntention>> selectContractRenewalIntention(Integer id) {
        Boolean b = checkParam(id);
        if(b){
                List<ContractRenewalIntention> list = staffContractService.selectContractRenewalIntention(id);
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 劳动合同到期续签通知书
     */
    @RequestMapping(value = "/getRenewalContract", method = RequestMethod.GET)
    @ApiOperation(value = "劳动合同到期续签通知书", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "form", required = true)
    public ResponseResult<List<RenewIntionAboutUser>> getRenewalContract() {
        Boolean b = checkParam(getUserSession());
        if(b){
            List<RenewIntionAboutUser> list = staffContractService.getRenewalContract(getUserSession().getArchiveId());
            return new ResponseResult<>(list,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 展示部门下的续签意向表
     */
    @RequestMapping(value = "/selectContractRenewalIntentionByOrg", method = RequestMethod.GET)
    @ApiOperation(value = "展示部门下的续签意向表", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "档案id", paramType = "form", required = true)
    public ResponseResult<PageResult<ContractRenewalIntention>> selectContractRenewalIntention(@RequestParam List<Integer> orgId,Integer currentPage,Integer pageSIze) {

        Boolean b = checkParam(orgId,pageSIze,currentPage);
        if(b){
                PageResult < ContractRenewalIntention > contractRenewalIntentionPageResult = staffContractService.selectContractRenewalIntentionByOrg ( orgId, pageSIze, currentPage );
                    return new ResponseResult<>(contractRenewalIntentionPageResult,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 同意续签
     */
    @RequestMapping(value = "/agreeRenew", method = RequestMethod.GET)
    @ApiOperation(value = "同意续签", notes = "hkt")
//    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult agreeRenew(Integer xuqianId) {
        Boolean b = checkParam(xuqianId);
        if(b){
                staffContractService.agreeRenew(xuqianId);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 拒绝续签
     */
    @RequestMapping(value = "/rejectRenew", method = RequestMethod.GET)
    @ApiOperation(value = "不同意续签", notes = "hkt")
//    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult rejectRenew(Integer xuqianId) {
        Boolean b = checkParam(xuqianId);
        if(b) {
            staffContractService.rejectRenew(xuqianId);
            return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 生成合同报表
     */
    @RequestMapping(value = "/createContractForm", method = RequestMethod.GET)
    @ApiOperation(value = "生成合同报表", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "机构id", paramType = "form", required = true)
    public ResponseResult<PageResult<ContractFormVo>> createContractForm(@RequestParam List<Integer> list,Integer pageSize,Integer currentPage) {
        Boolean b = checkParam(list,pageSize,currentPage,getUserSession ());
        if(b){
                PageResult < ContractFormVo > userArchiveVoPageResult = staffContractService.createContractForm ( list,pageSize,currentPage,getUserSession () );
                    return new ResponseResult<>(userArchiveVoPageResult,CommonCode.SUCCESS);

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
    private ResponseResult failResponseResult(String message) {
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }
}
