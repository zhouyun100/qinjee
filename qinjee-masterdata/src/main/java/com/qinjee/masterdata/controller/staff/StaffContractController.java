package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import com.qinjee.masterdata.model.entity.LaborContract;
import com.qinjee.masterdata.model.entity.LaborContractChange;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.staff.LaborContractChangeVo;
import com.qinjee.masterdata.model.vo.staff.LaborContractVo;
import com.qinjee.masterdata.service.staff.IStaffContractService;
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
@RequestMapping("/staffcon")
@Api(tags = "【人员管理】合同相关接口")
public class StaffContractController extends BaseController {
    @Autowired
    private IStaffContractService staffContractService;
    /**
     * 展示未签合同
     */
    @RequestMapping(value = "/selectNoLaborContractU", method = RequestMethod.GET)
    @ApiOperation(value = "展示未签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),

    })
    public ResponseResult<PageResult<UserArchive>> selectLaborContract(Integer orgId,Integer currentPage, Integer pageSize
                                                                                                        ) {
        return staffContractService.selectNoLaborContract(orgId,currentPage,pageSize);
    }

    /**
     * 新签单个合同（合同编号支持输入，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     */

    @RequestMapping(value = "/insertLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "新签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
            @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    })
    public ResponseResult insertLaborContract(LaborContractVo laborContractVo,Integer id ) {
        Integer archiveId = userSession.getArchiveId();
        return  staffContractService.insertLaborContract(laborContractVo,id,archiveId);
    }

    /**id
     * 批量新签合同（合同编号不支持输入，需系统自动生成，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     */
    /**
     * 这里需要进行人员信息的查询，然后将属性取出来塞进合同集合中。
     * @param list
     * @return
     */
    @RequestMapping(value = "/insertLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量新签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractVo", value = "合同vo表", paramType = "form", required = true),
            @ApiImplicitParam(name = "list", value = "档案id集合", paramType = "query", required = true)
    })
    public ResponseResult insertLaborContractBatch(LaborContractVo laborContractVo,List<Integer> list) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.insertLaborContractBatch(laborContractVo,list,archiveId);
    }

    /**
     * 保存合同（对应的是保存，就是合同已经编辑，此时字段是否已签为未签）
     * 这个跟新增合同一样的，只是合同状态不同
     */

    public ResponseResult SaveLaborContract(LaborContractVo laborContractVo,Integer id ) {
        Integer archiveId = userSession.getArchiveId();
        return  staffContractService.SaveLaborContract(laborContractVo,id,archiveId);
    }

    /**
     * 删除合同
     */
    @RequestMapping(value = "/deleteLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "删除合同", notes = "hkt")
    @ApiImplicitParam(name = "LaborContractid", value = "合同id", paramType = "query", required = true)
    public ResponseResult deleteLaborContract(Integer laborContractid) {
        return staffContractService.deleteLaborContract(laborContractid);
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
            @ApiImplicitParam(name = "laborContract", value = "合同信息", paramType = "form", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult updatelaborContract(LaborContract laborContract, LaborContractChangeVo laborContractChangeVo, Integer id) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.updatelaborContract(laborContract,laborContractChangeVo,id,archiveId);

    }

    /**
     * 合同续签
     * 续签有自己的页面，这个续签是合同的一种状态，统一放在合同表中维护,删除也可以根据合同id来删除
     */

    @RequestMapping(value = "/insertReNewLaborContract", method = RequestMethod.POST)
    @ApiOperation(value = "续签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
            @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult insertReNewLaborContract(LaborContractVo laborContractVo,Integer id,
                                                   LaborContractChangeVo laborContractChangeVo) {
        //续签时候将续签次数在原有基础上加一
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.insertReNewLaborContract(laborContractVo,id,laborContractChangeVo,archiveId);
    }

    /**
     * 批量续签
     */
    @RequestMapping(value = "/insertReNewLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "续签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractVo", value = "合同VO表", paramType = "form", required = true),
            @ApiImplicitParam(name = "list", value = "档案id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult insertReNewLaborContractBatch(LaborContractVo laborContractVo,List<Integer> list,
                                                   LaborContractChangeVo laborContractChangeVo) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.insertReNewLaborContractBatch(laborContractVo,list,laborContractChangeVo,archiveId);
    }
    /**
     * 展示已签合同的人员信息
     */
    @RequestMapping(value = "/selectLaborContractserUser", method = RequestMethod.GET)
    @ApiOperation(value = "展示已签合同的人员信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),

    })
    public ResponseResult<PageResult<UserArchive>> selectLaborContractserUser(Integer orgId,Integer currentPage, Integer pageSize
    ) {
        return staffContractService.selectLaborContractserUser(orgId,currentPage,pageSize);
    }

    /**
     * 终止合同信息
     */
    @RequestMapping(value = "/endlaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "终止合同信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult EndlaborContract(LaborContractChangeVo laborContractChangeVo, Integer id) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.endlaborContract(laborContractChangeVo,id,archiveId);

    }
    /**
     * 批量终止合同
     */
    @RequestMapping(value = "/endlaborContractBatch", method = RequestMethod.GET)
    @ApiOperation(value = "批量终止合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "合同id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult EndlaborContract(LaborContractChangeVo laborContractChangeVo, List<Integer> list) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.endlaborContractBatch(laborContractChangeVo,list,archiveId);

    }
    /**
     * 解除合同信息
     */
    @RequestMapping(value = "/looselaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "解除合同信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractId", value = "合同id", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult looselaborContract(LaborContractChangeVo laborContractChangeVo, Integer id) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.looselaborContract(laborContractChangeVo,id,archiveId);

    }
    /**
     * 批量解除合同信息
     */
    @RequestMapping(value = "/looselaborContractBatch", method = RequestMethod.GET)
    @ApiOperation(value = "批量解除合同信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "合同id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "laborContractChangeVo", value = "合同变更Vo类", paramType = "form", required = true)
    })
    public ResponseResult looselaborContractBatch(LaborContractChangeVo laborContractChangeVo, List<Integer> list) {
        Integer archiveId = userSession.getArchiveId();
        return staffContractService.looselaborContractBatch(laborContractChangeVo,list,archiveId);

    }
    /**
     * 展示合同的变更记录，对合同的状态进行变更的，都应该有变更记录
     */

    @RequestMapping(value = "/selectLaborContractchange", method = RequestMethod.GET)
    @ApiOperation(value = "展示合同的变更记录", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "合同id", paramType = "query", required = true)
    public ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id) {
       return staffContractService.selectLaborContractchange(id);
    }

    /**
     * 发送续签意向表
     * 实现思路：在数据库为主一张意向书表，设置好表里面需要展示的信息。将id放进消息队列中，前端通过队列去取信息。然后展示。
     */
    @RequestMapping(value = "/insertLaborContractIntention", method = RequestMethod.GET)
    @ApiOperation(value = "发送续签意向表", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult insertLaborContractIntention(Integer id) {
        return staffContractService.insertLaborContractIntention(id);
    }

    /**
     * 续签反馈
     */
    @RequestMapping(value = "/insertContractRenewalIntention", method = RequestMethod.POST)
    @ApiOperation(value = "续签反馈", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "续签反馈表", paramType = "form", required = true)
    public ResponseResult insertResponseLaborContract(ContractRenewalIntention contractRenewalIntention){

        return staffContractService.insertContractRenewalIntention(contractRenewalIntention);
    }
    /**
     * 展示我的续签意向
     */
    @RequestMapping(value = "/selectContractRenewalIntention", method = RequestMethod.POST)
    @ApiOperation(value = "展示我的续签意向", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "档案id", paramType = "form", required = true)
    public ResponseResult selectContractRenewalIntention( Integer id){

        return staffContractService.selectContractRenewalIntention(id);
    }
    /**
     * 同意续签
     */
    @RequestMapping(value = "/agreeRenew", method = RequestMethod.POST)
    @ApiOperation(value = "同意续签", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult agreeRenew(ContractRenewalIntention contractRenewalIntention){

        return staffContractService.agreeRenew(contractRenewalIntention);
    }
    /**
     * 拒绝续签
     */
    @RequestMapping(value = "/rejectRenew", method = RequestMethod.POST)
    @ApiOperation(value = "不同意续签", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult rejectRenew(ContractRenewalIntention contractRenewalIntention){

        return staffContractService.rejectRenew(contractRenewalIntention);
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
    /**
     * 查询在职员工的人数
     */
    @RequestMapping(value = "/selectArcNumberIn", method = RequestMethod.POST)
    @ApiOperation(value = "查询在职员工的人数", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "机构id", paramType = "form", required = true)
    public ResponseResult selectArcNumberIn(Integer id){

        return staffContractService.selectArcNumberIn(id);
    }
    /**
     * 查询机构下合同即将到期的员工
     */
    @RequestMapping(value = "/selectArcDeadLine", method = RequestMethod.POST)
    @ApiOperation(value = "查询机构下合同即将到期的员工", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "机构id", paramType = "form", required = true)
    public ResponseResult selectArcDeadLine(Integer id){

        return staffContractService.selectArcDeadLine(id);
    }
/**合同状态  新签、变更   续签、解除、终止
 *  合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
 *   审批状态  未提交、审批中、已审批
 *   导入导出接口可以复用导入导出模块
 *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
 */
}
