package com.qinjee.masterdata.controller.organization;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 机构controller
 * @createTime 2019年09月10日 10:19:00
 */
@RequestMapping("/organization")
@RestController
@Api(tags = "【机构管理】机构接口")
public class OrganizationController extends BaseController {
    private static Logger logger = LogManager.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";


    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("/addOrganization")
    @ApiOperation(value = "ok，新增机构", notes = "ok")
    public ResponseResult addOrganization(@RequestParam("orgName") String orgName, @RequestParam("orgCode") String orgCode, @RequestParam("orgType") String orgType, @RequestParam(value = "orgParentId") String orgParentId, @RequestParam(value = "orgManagerId", required = false) String orgManagerId) {
        Boolean b = checkParam(orgName, orgType, orgParentId);
        if (b) {
            organizationService.addOrganization(orgName, orgCode, orgType, orgParentId, orgManagerId, getUserSession());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/editOrganization")
    @ApiOperation(value = "ok，编辑机构", notes = "机构编码待验证")
    public ResponseResult editOrganization(@RequestParam("orgCode") String orgCode, @RequestParam("orgId") String orgId, @RequestParam("orgName") String orgName, @RequestParam("orgType") String orgType, @RequestParam("orgParentId") String orgParentId, @RequestParam(value = "orgManagerId", required = false) String orgManagerId) {
        Boolean b = checkParam(orgCode, orgId, orgParentId);
        if (b) {
            organizationService.editOrganization(orgCode, orgId, orgName, orgType, orgParentId, orgManagerId, getUserSession());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/deleteOrganizationById")
    @ApiOperation(value = "ok，删除机构", notes = "ok")
    public ResponseResult deleteOrganizationById(@ApiParam(value = "机构id列表") @RequestBody List<Integer> orgIds) {
        Boolean b = checkParam(orgIds);
        if (b) {
            organizationService.deleteOrganizationById(orgIds, getUserSession());
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * @Description: 查询用户下所有机构及子机构
     * @Param:
     * @return:
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @ApiOperation(value = "ok，获取机构树", notes = "ok")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "isEnable", value = "是否包含封存：0不包含（默认）、1 包含", paramType = "query", dataType = "short")
    })
    @GetMapping("/getAllOrganizationTree")
    public ResponseResult<PageResult<OrganizationVO>> getAllOrganizationTree(@RequestParam(value = "isEnable") Short isEnable) {
        Boolean b = checkParam(isEnable);
        if (b) {
            if (isEnable != 0) {
                isEnable = null;
            }
            List<OrganizationVO> organizationVOList = organizationService.getAllOrganizationTree(getUserSession().getArchiveId(), isEnable);
            PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
            return new ResponseResult(pageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/getOrganizationPageList")
    @ApiOperation(value = "ok，分页按条件查询用户下所有的机构(包含子孙)", notes = "ok")
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationPageList(@RequestBody OrganizationPageVo organizationPageVo) {
        Boolean b = checkParam(organizationPageVo);
        if (b) {
            Short isEnable = organizationPageVo.getIsEnable();
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            organizationPageVo.setIsEnable(isEnable);
            PageResult<OrganizationVO> pageResult = organizationService.getAllOrganizationPageList(organizationPageVo, getUserSession());
            return new ResponseResult(pageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/getDirectOrganizationPageList")
    @ApiOperation(value = "ok，分页查询下级直属机构", notes = "ok")
    public ResponseResult<PageResult<OrganizationVO>> getDirectOrganizationPageList(@RequestBody OrganizationPageVo organizationPageVo) {
        Boolean b = checkParam(organizationPageVo);
        if (b) {
            Short isEnable = organizationPageVo.getIsEnable();
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            organizationPageVo.setIsEnable(isEnable);
            UserSession userSession = getUserSession();
            PageResult<OrganizationVO> pageResult = organizationService.getDirectOrganizationPageList(organizationPageVo, userSession);
            return new ResponseResult<>(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    /**
     * 默认显示所有层级，显示多少级由前端控制，后端只要返回全部结果即可
     * 图数据结构需要含有机构负责人照片、姓名、机构名称、实有人数和编制人数
     *
     * @return
     */
    //TODO 编制人数暂时不考虑
    //TODO 递归层数控制
    @ApiOperation(value = "ok，获取机构图", notes = "ok")
    @GetMapping("/getOrganizationGraphics")
    public ResponseResult<List<OrganizationVO>> getOrganizationGraphics(@RequestParam("layer") @ApiParam(value = "机构图层数，默认显示2级", example = "2") Integer layer,
                                                                        @RequestParam("isContainsCompiler") @ApiParam(value = "是否显示编制人数", example = "false") boolean isContainsCompiler,
                                                                        @RequestParam("isContainsActualMembers") @ApiParam(value = "是否显示实有人数", example = "false") boolean isContainsActualMembers,
                                                                        @RequestParam("orgId") @ApiParam(value = "机构id", example = "1") Integer orgId,
                                                                        @RequestParam("isEnable") @ApiParam(value = "是否包含封存：0不包含（默认）、1 包含", example = "0") Short isEnable) {
        Boolean b = checkParam(layer, isContainsCompiler, isContainsActualMembers, orgId, isEnable);
        if (b) {
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            List<OrganizationVO> pageResult = organizationService.getOrganizationGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, orgId, isEnable);
            return new ResponseResult(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，获取机构岗位树", notes = "ok")
    @GetMapping("/getOrganizationPostTree")
    public ResponseResult<List<OrganizationVO>> getOrganizationPostTree(@ApiParam(value = "是否不包含封存：0不包含（默认）、1 包含", example = "0") @RequestParam(value = "isEnable") Short isEnable) {
        Boolean b = checkParam(isEnable);
        if (b) {
            if (isEnable != 0) {
                isEnable = null;
            }
            List<OrganizationVO> orgList = organizationService.getOrganizationPostTree(getUserSession(), isEnable);
            ResponseResult responseResult = new ResponseResult();
            responseResult.setResult(orgList);
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 机构下有人，不能封存
     *
     * @param orgIds
     * @return
     */
    @PostMapping("/lockOrganizationByIds")
    @ApiOperation(value = "ok，封存机构，设为0", notes = "ok")
    public ResponseResult lockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        if (checkParam(orgIds)) {
            organizationService.sealOrganization(orgIds, Short.parseShort("0"));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/unlockOrganizationByIds")
    @ApiOperation(value = "ok，解封机构，设为1", notes = "ok")
    public ResponseResult unlockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        if (checkParam(orgIds)) {
            organizationService.sealOrganization(orgIds, Short.parseShort("1"));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    // String filePath,  List<Integer> orgIds
    @PostMapping("/exportOrganization")
    @ApiOperation("机构导出，demo {\"orgId\":28,\"orgIds\":[14,25]}")
    public ResponseResult exportOrganization(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
        if (checkParam(paramMap)) {
            List<Integer> orgIds = null;
            Integer orgId = null;
            if (paramMap.get("orgIds") != null && paramMap.get("orgIds") instanceof List) {
                orgIds = (List<Integer>) paramMap.get("orgIds");
            }
            if (paramMap.get("orgId") != null && paramMap.get("orgId") instanceof Integer) {
                orgId = (Integer) paramMap.get("orgId");
            }
            List<OrganizationVO> organizationVOList = organizationService.exportOrganization(orgId, orgIds, getUserSession().getArchiveId());
            List<OrganizationVO> dataList = new ArrayList<>();
            //将部门类型转变为中文
            for (OrganizationVO org : organizationVOList) {
                if ("GROUP".equalsIgnoreCase(org.getOrgType())) {
                    org.setOrgType("集团");
                } else if ("UNIT".equalsIgnoreCase(org.getOrgType())) {
                    org.setOrgType("单位");
                } else if ("DEPT".equalsIgnoreCase(org.getOrgType())) {
                    org.setOrgType("部门");
                }
                dataList.add(org);
            }
            response.setHeader("fileName", URLEncoder.encode("orgDefualt.xls", "UTF-8"));
            Workbook workbook = DefaultExcelBuilder.of(OrganizationVO.class).build(dataList);

            AttachmentExportUtil.export(workbook, "orgDefualt", response);
            //只能返回null
            return null;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @PostMapping("/uploadAndCheck")
    @ApiOperation(value = "ok,导入机构excel并校验", notes = "ok")
    public ResponseResult uploadAndCheck(MultipartFile multfile, HttpServletResponse response) throws Exception {
        //参数判空校验
        if (checkParam(multfile)) {
            return organizationService.uploadAndCheck(multfile, getUserSession(), response);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/exportError2Txt")
    @ApiOperation(value = "ok,导出错误信息到txt", notes = "ok")
    public ResponseResult exportError2Txt(String redisKey, HttpServletResponse response) throws Exception {
        if (checkParam(redisKey)) {
            String errorData = redisClusterService.get(redisKey.trim());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition",
                "attachment;filename=\"" + URLEncoder.encode("errorInfo.txt", "UTF-8") + "\"");
            response.setHeader("fileName", URLEncoder.encode("errorInfo.txt", "UTF-8"));
            response.getOutputStream().write(errorData.getBytes());
            //是否只能返回null
            return null;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/cancelImport")
    @ApiOperation(value = "ok,取消导入(将数据从redis中删除)")
    public ResponseResult cancelImport(@RequestParam("redisKey") String redisKey, @RequestParam("errorInfoKey") String errorInfoKey) {
        if (checkParam(redisKey)) {
            organizationService.cancelImport(redisKey.trim(), errorInfoKey.trim());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @GetMapping("/importToDatabase")
    @ApiOperation(value = "ok,导入机构入库")
    public ResponseResult importToDatabase(@RequestParam("orgExcelRedisKey") String orgExcelRedisKey) {
        if (checkParam(orgExcelRedisKey)) {
            organizationService.importToDatabase(orgExcelRedisKey, getUserSession());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * @Description: AorgId:2                                        AorgId：1
     * BorgId:1    传参  在后端进行排序id更新-----》   BorgId：2
     * CorgId:3                                        CorgId：3
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @PostMapping("/sortOrganization")
    @ApiOperation(value = "ok，机构排序，只能同一级别下机构排序（需要将该级下所有机构的id按顺序传参）", notes = "ok")
    public ResponseResult sortOrganizationInOrg(@RequestBody LinkedList<Integer> orgIds) {
        //参数校验
        if (checkParam(orgIds)) {
            organizationService.sortOrganization(orgIds);
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    //TODO
    @GetMapping("/getUserArchiveListByUserName")
    @ApiOperation(value = "ok,机构负责人查询，如果带负责人姓名，则根据姓名模糊查询，不带参则全量查询", notes = "需要调用人员接口")
    public ResponseResult<List<UserArchiveVo>> getUserArchiveListByUserName(@ApiParam(value = "姓名", example = "张三", required = true) @RequestParam(value = "userName", required = false) String userName) {
        if (checkParam(userName)) {
            List<UserArchiveVo> users = organizationService.getUserArchiveListByUserName(userName);
            return new ResponseResult(users);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，划转机构,参数demo  {\"orgIds\":[1001,1002],\"targetOrgId\":1003}")
    @PostMapping("/transferOrganization")
    //TODO 还需要将人员进行划转
    public ResponseResult transferOrganization(@RequestBody Map<String, Object> paramMap) {
        //校验参数
        if (checkParam(paramMap)) {
            List<Integer> orgIds = (List<Integer>) paramMap.get("orgIds");
            Integer targetOrgId = (Integer) paramMap.get("targetOrgId");
            //校验参数
            if (checkParam(orgIds, targetOrgId)) {
                organizationService.transferOrganization(orgIds, targetOrgId, getUserSession());
                return ResponseResult.SUCCESS();
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    //TODO
    @PostMapping("/mergeOrganization")
    @ApiOperation(value = "ok，合并机构,传参demo  {\"newOrgName\":\"新机构名称\",\"orgIds\":[1001,1002],\"parentOrgId\":1003}", notes = "调通，需维护人员")
    public ResponseResult mergeOrganization(@RequestBody Map<String, Object> paramMap) {
        //校验参数
        if (checkParam(paramMap)) {
            List<Integer> orgIds = (List<Integer>) paramMap.get("orgIds");
            Integer parentOrgId = (Integer) paramMap.get("parentOrgId");
            String newOrgName = (String) paramMap.get("newOrgName");
            //校验参数
            if (checkParam(orgIds, parentOrgId, newOrgName)) {
                //进行机构合并
                organizationService.mergeOrganization(newOrgName, parentOrgId, orgIds, getUserSession());
                return ResponseResult.SUCCESS();
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "根据orgId生成机构编码（用于新增机构时获取新机构编码）")
    @GetMapping("/generateOrgCode")
    public ResponseResult generateOrgCode(Integer orgId) {
        //校验参数
        if (checkParam(orgId)) {
                String orgCode = organizationService.generateOrgCode(orgId);
                return new ResponseResult<>(orgCode);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
}
