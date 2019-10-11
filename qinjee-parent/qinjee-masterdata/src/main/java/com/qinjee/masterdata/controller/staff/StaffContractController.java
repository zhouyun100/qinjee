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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
     * 展示未签合同
     */
    @RequestMapping(value = "/selectNoLaborContractU", method = RequestMethod.GET)
    @ApiOperation(value = "展示未签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),
    })
    public ResponseResult<PageResult<UserArchive>> selectLaborContract(Integer orgId, Integer currentPage, Integer pageSize
    ) {
        Boolean b = checkParam(orgId, currentPage, pageSize);
        if (b) {
            try {
                PageResult<UserArchive> pageResult =
                        staffContractService.selectNoLaborContract(orgId, currentPage, pageSize);
                if (pageResult != null) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return failResponseResult("没有未签合同");
            } catch (Exception e) {
                return failResponseResult("显示部门下的未签合同失败");
            }
        }
        return failResponseResult("部门id错误");

    }

    /**合同状态  新签、变更   续签、解除、终止
     *  合同标识  有效、无效（根据合同状态与合同起始状态确定是否有效）
     *   审批状态  未提交、审批中、已审批
     *   导入导出接口可以复用导入导出模块
     *   导入校验，此时先把excel转成list，需要对list的各个属性进行一一校验，这个规则还需要产品确定，交给前端进行筛选校验
     */

    /**
     * 展示已签合同的人员信息
     */
    @RequestMapping(value = "/selectLaborContractserUser", method = RequestMethod.GET)
    @ApiOperation(value = "展示已签合同的人员信息", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "机构ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "isEnable", value = "是否有效", paramType = "query", required = true),
            @ApiImplicitParam(name = "list", value = "合同状态", paramType = "query", required = true),

    })
    public ResponseResult<PageResult<UserArchive>> selectLaborContractserUser(Integer orgId, Integer currentPage,
                                                                              Integer pageSize,Boolean isEnable,
                                                                              List<String> status
    ) {
        Boolean b = checkParam(orgId, currentPage, pageSize,isEnable,status);
        if (b) {
            try {
                PageResult<UserArchive> pageResult =
                        staffContractService.selectLaborContractserUser(orgId, currentPage, pageSize,isEnable,status);
                if (pageResult != null) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return failResponseResult("没有已签合同");
            } catch (Exception e) {
                return failResponseResult("显示部门下的" +
                        "合同失败");
            }
        }
        return failResponseResult("部门id错误");

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
    public ResponseResult insertLaborContract(@Valid LaborContractVo laborContractVo, Integer id) {
        Boolean b = checkParam(laborContractVo,id,getUserSession());
        if(b){
            try {
                staffContractService.insertLaborContract(laborContractVo, id, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新签合同失败");
            }

        }
        return  failResponseResult("参数错误");

    }

    /**id
     * 批量新签合同（合同编号不支持输入，需系统自动生成，对应的是提交，就是合同已经生效，此时字段是否已签改为是否已签）
     * 这里需要进行人员信息的查询，然后将属性取出来塞进合同集合中。
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "/insertLaborContractBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量新签合同", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "laborContractVo", value = "合同vo表", paramType = "form", required = true),
            @ApiImplicitParam(name = "list", value = "档案id集合", paramType = "query", required = true)
    })
    public ResponseResult insertLaborContractBatch(LaborContractVo laborContractVo, List<Integer> list) {
        Boolean b = checkParam(laborContractVo, list, getUserSession());
        if (b) {
            try {
                staffContractService.insertLaborContractBatch(laborContractVo, list, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("批量新签合同失败");
            }

        }
        return failResponseResult("参数错误");
    }

    /**
     * 保存合同（对应的是保存，就是合同已经编辑，此时字段是否已签为未签）
     * 这个跟新增合同一样的，只是合同状态不同
     */

    public ResponseResult SaveLaborContract(LaborContractVo laborContractVo, Integer id) {
        Boolean b = checkParam(laborContractVo,id,getUserSession());
        if(b){
            try {
                staffContractService.SaveLaborContract(laborContractVo, id, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("保存合同失败");
            }

        }
        return  failResponseResult("参数错误");

    }

    /**
     * 删除合同
     */
    @RequestMapping(value = "/deleteLaborContract", method = RequestMethod.GET)
    @ApiOperation(value = "删除合同", notes = "hkt")
    @ApiImplicitParam(name = "LaborContractid", value = "合同id", paramType = "query", required = true)
    public ResponseResult deleteLaborContract(Integer laborContractid) {
        Boolean b = checkParam(laborContractid);
        if(b){
            try {
                staffContractService.deleteLaborContract(laborContractid);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("删除合同失败");
            }

        }
        return  failResponseResult("参数错误");
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
    public ResponseResult updatelaborContract(@Valid LaborContract laborContract,
                                              @Valid LaborContractChangeVo laborContractChangeVo,
                                              Integer id) {
        Boolean b = checkParam(laborContract,laborContractChangeVo,id,getUserSession());
        if(b){
            try {
                staffContractService.updatelaborContract(laborContract, laborContractChangeVo, id, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("更新合同失败");
            }

        }
        return  failResponseResult("参数错误");
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
    public ResponseResult insertReNewLaborContract(@Valid LaborContractVo laborContractVo, Integer id,
                                                   @Valid LaborContractChangeVo laborContractChangeVo) {

        Boolean b = checkParam(laborContractVo,laborContractChangeVo,id,getUserSession());
        if(b){
            try {
                staffContractService.insertReNewLaborContract(laborContractVo, id, laborContractChangeVo, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("续签合同失败");
            }

        }
        return  failResponseResult("参数错误");
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
    public ResponseResult insertReNewLaborContractBatch(@Valid LaborContractVo laborContractVo, List<Integer> list,
                                                        @Valid LaborContractChangeVo laborContractChangeVo) {
        Boolean b = checkParam(laborContractVo,laborContractChangeVo,list,getUserSession());
        if(b){
            try {
                staffContractService.insertReNewLaborContractBatch(laborContractVo, list, laborContractChangeVo, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("批量续签合同失败");
            }

        }
        return  failResponseResult("参数错误");
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
    public ResponseResult EndlaborContract(@Valid LaborContractChangeVo laborContractChangeVo, Integer id) {
        Boolean b = checkParam(laborContractChangeVo,id,getUserSession());
        if(b){
            try {
                staffContractService.endlaborContract(laborContractChangeVo, id,getUserSession());
            } catch (Exception e) {
                return failResponseResult("终止合同失败");
            }

        }
        return  failResponseResult("参数错误");
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
    public ResponseResult EndlaborContract(@Valid LaborContractChangeVo laborContractChangeVo, List<Integer> list) {
        Boolean b = checkParam(laborContractChangeVo,list,getUserSession());
        if(b){
            try {
                staffContractService.endlaborContractBatch(laborContractChangeVo, list, getUserSession());
            } catch (Exception e) {
                return failResponseResult("批量终止合同失败");
            }
        }
        return  failResponseResult("参数错误");
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
    public ResponseResult looselaborContract(@Valid LaborContractChangeVo laborContractChangeVo, Integer id) {
        Boolean b = checkParam(laborContractChangeVo,id,getUserSession());
        if(b){
            try {
                staffContractService.looselaborContract(laborContractChangeVo, id, getUserSession());
            } catch (Exception e) {
                return failResponseResult("解除合同失败");
            }
        }
        return  failResponseResult("参数错误");
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
    public ResponseResult looselaborContractBatch(@Valid LaborContractChangeVo laborContractChangeVo, List<Integer> list) {
        Boolean b = checkParam(laborContractChangeVo,list,getUserSession());
        if(b){
            try {
                staffContractService.looselaborContractBatch(laborContractChangeVo, list, getUserSession());
            } catch (Exception e) {
                return failResponseResult("批量解除合同失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 展示合同的变更记录，对合同的状态进行变更的，都应该有变更记录
     */

    @RequestMapping(value = "/selectLaborContractchange", method = RequestMethod.GET)
    @ApiOperation(value = "展示合同的变更记录", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "合同id", paramType = "query", required = true)
    public ResponseResult<List<LaborContractChange>> selectLaborContractchange(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                List<LaborContractChange> list = staffContractService.selectLaborContractchange(id);
                if(!CollectionUtils.isEmpty(list)){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }
                return failResponseResult("此合同没有变更记录");
            } catch (Exception e) {
                return failResponseResult("查看合同变更失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 发送续签意向表
     * 实现思路：拿到续签人员的档案id，根据档案中信息设置续签意向表。
     */
    @RequestMapping(value = "/insertLaborContractIntention", method = RequestMethod.GET)
    @ApiOperation(value = "发送续签意向表", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "档案id", paramType = "query", required = true)
    public ResponseResult insertLaborContractIntention(Integer id) {
        Boolean b = checkParam(id,getUserSession());
        if(b){
            try {
                staffContractService.insertLaborContractIntention(id,getUserSession());

            } catch (Exception e) {
                return failResponseResult("发送续签意向表失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 续签反馈
     */
    @RequestMapping(value = "/updateContractRenewalIntention", method = RequestMethod.POST)
    @ApiOperation(value = "续签反馈", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "续签反馈表", paramType = "form", required = true)
    public ResponseResult insertResponseLaborContract(@Valid ContractRenewalIntention contractRenewalIntention) {
        Boolean b = checkParam(contractRenewalIntention);
        if(b){
            try {
                staffContractService.updateContractRenewalIntention(contractRenewalIntention);

            } catch (Exception e) {
                return failResponseResult("获取续签反馈失败");
            }
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 展示我的续签意向
     */
    @RequestMapping(value = "/selectContractRenewalIntention", method = RequestMethod.POST)
    @ApiOperation(value = "展示我的续签意向", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "档案id", paramType = "form", required = true)
    public ResponseResult<ContractRenewalIntention> selectContractRenewalIntention(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                staffContractService.selectContractRenewalIntention(id);
            } catch (Exception e) {
                return failResponseResult("展示我的续签意向失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 同意续签
     */
    @RequestMapping(value = "/agreeRenew", method = RequestMethod.POST)
    @ApiOperation(value = "同意续签", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult agreeRenew(@Valid ContractRenewalIntention contractRenewalIntention) {
        Boolean b = checkParam(contractRenewalIntention);
        if(b){
            try {
                staffContractService.agreeRenew(contractRenewalIntention);
            } catch (Exception e) {
                return failResponseResult("同意续签失败");
            }
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 拒绝续签
     */
    @RequestMapping(value = "/rejectRenew", method = RequestMethod.POST)
    @ApiOperation(value = "不同意续签", notes = "hkt")
    @ApiImplicitParam(name = "ContractRenewalIntention", value = "ContractRenewalIntention类", paramType = "form", required = true)
    public ResponseResult rejectRenew(ContractRenewalIntention contractRenewalIntention) {
        Boolean b = checkParam(contractRenewalIntention);
        if(b){
            try {
                staffContractService.rejectRenew(contractRenewalIntention);
            } catch (Exception e) {
                return failResponseResult("拒绝续签失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 查询在职员工的人数
     */
    @RequestMapping(value = "/selectArcNumberIn", method = RequestMethod.POST)
    @ApiOperation(value = "查询在职员工的人数", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "机构id", paramType = "form", required = true)
    public ResponseResult selectArcNumberIn(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                staffContractService.selectArcNumberIn(id);
            } catch (Exception e) {
                return failResponseResult("查询在职员工人数失败");
            }
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 查询机构下合同即将到期的员工
     */
    @RequestMapping(value = "/selectArcDeadLine", method = RequestMethod.POST)
    @ApiOperation(value = "查询机构下合同即将到期的员工", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "机构id", paramType = "form", required = true)
    public ResponseResult<List<UserArchive>> selectArcDeadLine(Integer id) {
        Boolean b = checkParam(id);
        if(b){
            try {
                staffContractService.selectArcDeadLine(id);
            } catch (Exception e) {
                return failResponseResult("查询合同即将到期人数失败");
            }
        }
        return  failResponseResult("参数错误");
    }


    /**
     * 检验参数
     *
     * @param params
     * @return
     */
    public Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 错误返回值
     *
     * @param message
     * @return
     */
    public ResponseResult failResponseResult(String message) {
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }
}