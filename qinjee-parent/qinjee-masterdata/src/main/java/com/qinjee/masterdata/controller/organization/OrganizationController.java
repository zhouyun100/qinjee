package com.qinjee.masterdata.controller.organization;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationMergeBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationPageBO;
import com.qinjee.masterdata.model.vo.organization.bo.OrganizationTransferBO;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.sys.SysDictService;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping("/organization")
@RestController
@Api(tags = "【机构管理】机构接口")
/**
 * 在操作（删除、划转、合并、解封、封存）机构之前，根据用户勾选的机构id，查询出这些机构id下实际存在的机构与用户有权的机构进行对比，如果相等则代表用户有权操作，不等则提示权限不足（存在下级未授权机构）
 */
public class OrganizationController extends BaseController {
    private static Logger logger = LogManager.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    @Autowired
    private SysDictService sysDictService;


    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (param instanceof UserSession) {
                if (null == param || "".equals(param)) {
                    ExceptionCast.cast(CommonCode.INVALID_SESSION);
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("/addOrganization")
    @ApiOperation(value = "ok，新增机构", notes = "phs")
    public ResponseResult addOrganization(@RequestParam("orgName") String orgName, @RequestParam("orgCode") String orgCode, @RequestParam("orgType") String orgType, @RequestParam(value = "orgParentId") String orgParentId, @RequestParam(value = "orgManagerId", required = false) String orgManagerId) {
        Boolean b = checkParam(orgName, orgType, orgParentId, getUserSession());
        if (b) {
            logger.info("新增机构：companyID:{},orgName：{}，orgCode：{}，orgType：{}，orgParentId：{}，orgManagerId：{}", userSession.getCompanyId(), orgName, orgCode, orgType, orgParentId, orgManagerId);
            long start = System.currentTimeMillis();
            organizationService.addOrganization(orgName, orgCode, orgType, orgParentId, orgManagerId, getUserSession());
            logger.info("新增机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/editOrganization")
    @ApiOperation(value = "ok，编辑机构", notes = "phs")
    public ResponseResult editOrganization(@RequestParam("orgCode") String orgCode, @RequestParam("orgId") String orgId, @RequestParam("orgName") String orgName, @RequestParam("orgType") String orgType, @RequestParam("orgParentId") String orgParentId, @RequestParam(value = "orgManagerId", required = false) String orgManagerId) {
        Boolean b = checkParam(orgCode, orgId, orgParentId, getUserSession());
        if (b) {
            long start = System.currentTimeMillis();
            logger.info("编辑机构：companyID:{},orgName：{}，orgCode：{}，orgType：{}，orgParentId：{}，orgManagerId：{}", userSession.getCompanyId(), orgName, orgCode, orgType, orgParentId, orgManagerId);
            organizationService.editOrganization(orgCode, orgId, orgName, orgType, orgParentId, orgManagerId, getUserSession());
            logger.info("编辑机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/deleteOrganizationById")
    @ApiOperation(value = "ok，删除机构", notes = "phs")
    public ResponseResult deleteOrganizationById(@ApiParam(value = "机构id列表") @RequestParam List<Integer> orgIds, @ApiParam(value = "是否级联删除岗位") @RequestParam boolean cascadeDeletePost) {
        Boolean b = checkParam(orgIds, getUserSession());
        if (b) {
            logger.info("删除机构，orgIds》" + orgIds);
            long start = System.currentTimeMillis();
            organizationService.deleteOrganizationById(orgIds, cascadeDeletePost, getUserSession());
            logger.info("删除机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
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
    @ApiOperation(value = "ok，获取机构树", notes = "phs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isEnable", value = "是否包含封存：0不包含（默认）、1 包含", paramType = "query", dataType = "short")
    })
    @GetMapping("/getAllOrganizationTree")
    public ResponseResult<PageResult<OrganizationVO>> getAllOrganizationTree(@RequestParam(value = "isEnable") Short isEnable) {
        Boolean b = checkParam(isEnable, getUserSession());
        if (b) {
            if (isEnable != 0) {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            List<OrganizationVO> organizationVOList = organizationService.getAllOrganizationTree(getUserSession().getArchiveId(), isEnable);
            PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
            logger.info("获取机构树耗时：" + (System.currentTimeMillis() - start) + "ms");
            return new ResponseResult(pageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/getOrganizationPageList")
    @ApiOperation(value = "ok，分页按条件查询用户下所有的机构(包含子孙)", notes = "phs")
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationPageList(@RequestBody OrganizationPageBO organizationPageBO) {
        Boolean b = checkParam(organizationPageBO, getUserSession());
        if (b) {
            Short isEnable = organizationPageBO.getIsEnable();
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            organizationPageBO.setIsEnable(isEnable);
            PageResult<OrganizationVO> pageResult = organizationService.getAllOrganizationPageList(organizationPageBO, getUserSession());
            logger.info("分页按条件查询用户下所有的机构(包含子孙)耗时：" + (System.currentTimeMillis() - start) + "ms");
            return new ResponseResult(pageResult, CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/getDirectOrganizationPageList")
    @ApiOperation(value = "ok，分页查询下级直属机构", notes = "phs")
    public ResponseResult<PageResult<OrganizationVO>> getDirectOrganizationPageList(@RequestBody OrganizationPageBO organizationPageBO) {
        Boolean b = checkParam(organizationPageBO, getUserSession());
        if (b) {
            Short isEnable = organizationPageBO.getIsEnable();
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            organizationPageBO.setIsEnable(isEnable);
            UserSession userSession = getUserSession();
            PageResult<OrganizationVO> pageResult = organizationService.getDirectOrganizationPageList(organizationPageBO, userSession);
            logger.info("分页查询下级直属机构耗时：" + (System.currentTimeMillis() - start) + "ms");
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
        Boolean b = checkParam(layer, isContainsCompiler, isContainsActualMembers, orgId, isEnable, getUserSession());
        if (b) {
            if (isEnable == null || isEnable == 0) {
                isEnable = 0;
            } else {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            List<OrganizationVO> pageResult = organizationService.getOrganizationGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, orgId, isEnable);
            logger.info("获取机构图时耗时：" + (System.currentTimeMillis() - start) + "ms");
            return new ResponseResult(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，获取机构岗位树", notes = "ok")
    @GetMapping("/getOrganizationPostTree")
    public ResponseResult<List<OrganizationVO>> getOrganizationPostTree(@ApiParam(value = "是否不包含封存：0不包含（默认）、1 包含", example = "0") @RequestParam(value = "isEnable") Short isEnable) {
        Boolean b = checkParam(isEnable, getUserSession());
        if (b) {
            if (isEnable != 0) {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            List<OrganizationVO> orgList = organizationService.getOrganizationPostTree(getUserSession(), isEnable);
            ResponseResult responseResult = new ResponseResult();
            responseResult.setResult(orgList);
            logger.info("获取机构岗位树耗时：" + (System.currentTimeMillis() - start) + "ms");
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * @param orgIds
     * @return
     */
    @PostMapping("/lockOrganizationByIds")
    @ApiOperation(value = "ok，封存机构，设为0", notes = "ok")
    public ResponseResult lockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        if (checkParam(orgIds, getUserSession())) {
            long start = System.currentTimeMillis();
            organizationService.sealOrganization(getUserSession().getArchiveId(), orgIds, Short.parseShort("0"));
            logger.info("封存机构：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/unlockOrganizationByIds")
    @ApiOperation(value = "ok，解封机构，设为1", notes = "ok")
    public ResponseResult unlockOrganizationByIds(@RequestBody List<Integer> orgIds) {
        if (checkParam(orgIds, getUserSession())) {
            long start = System.currentTimeMillis();
            organizationService.sealOrganization(getUserSession().getArchiveId(), orgIds, Short.parseShort("1"));
            logger.info("解封机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    // String filePath,  List<Integer> orgIds
    @PostMapping("/exportOrganization")
    public ResponseResult exportOrganization(@RequestBody OrganizationExportBO orgExportBO, HttpServletResponse response) {
        if (checkParam(orgExportBO, getUserSession())) {
            long start = System.currentTimeMillis();

            List<OrganizationVO> organizationVOList = organizationService.exportOrganization(orgExportBO, getUserSession().getArchiveId());
            List<OrganizationVO> dataList = new ArrayList<>();
            // 导出时将"DEPT"转为“部门”
            for (OrganizationVO org : organizationVOList) {
                List<SysDict> orgTypeDic = sysDictService.searchSysDictListByDictType("ORG_TYPE");
                for (SysDict dict : orgTypeDic) {
                    if (null != dict.getDictCode() && dict.getDictCode().equalsIgnoreCase(org.getOrgType())) {
                        org.setOrgType(dict.getDictValue());
                    }
                }
                dataList.add(org);
            }

            Workbook workbook = DefaultExcelBuilder.of(OrganizationVO.class).build(dataList);
            AttachmentExportUtil.export(workbook, "组织机构信息", response);
            logger.info("导出岗位耗时：" + (System.currentTimeMillis() - start) + "ms");
            //TODO 只能返回null
            return null;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @PostMapping("/uploadAndCheck")
    @ApiOperation(value = "ok,导入机构excel并校验", notes = "ok")
    public ResponseResult uploadAndCheck(MultipartFile multfile) throws Exception {
        //参数判空校验
        if (checkParam(multfile, getUserSession())) {
            long start = System.currentTimeMillis();
            ResponseResult responseResult = organizationService.uploadAndCheck(multfile, getUserSession());
            logger.info("导入机构excel并校验耗时：" + (System.currentTimeMillis() - start) + "ms");
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/exportError2Txt")
    @ApiOperation(value = "ok,导出错误信息到txt", notes = "ok")
    public ResponseResult exportError2Txt(String redisKey, HttpServletResponse response) throws Exception {
        if (checkParam(redisKey, getUserSession())) {
            long start = System.currentTimeMillis();
            String errorData = redisClusterService.get(redisKey.trim());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode("机构导入错误校验信息.txt", "UTF-8") + "\"");
            response.setHeader("fileName", URLEncoder.encode("机构导入错误校验信息.txt", "UTF-8"));
            response.getOutputStream().write(errorData.getBytes());
            logger.info("导出错误信息到txt耗时：" + (System.currentTimeMillis() - start) + "ms");
            //是否只能返回null
            return null;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/cancelImport")
    @ApiOperation(value = "ok,取消导入(将数据从redis中删除)")
    public ResponseResult cancelImport(@RequestParam("redisKey") String redisKey, @RequestParam("errorInfoKey") String errorInfoKey) {
        if (checkParam(redisKey, getUserSession())) {
            organizationService.cancelImport(redisKey.trim(), errorInfoKey.trim());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @GetMapping("/importToDatabase")
    @ApiOperation(value = "ok,导入机构入库")
    public ResponseResult importToDatabase(@RequestParam("orgExcelRedisKey") String orgExcelRedisKey) {
        if (checkParam(orgExcelRedisKey, getUserSession())) {
            long start = System.currentTimeMillis();
            organizationService.importToDatabase(orgExcelRedisKey, getUserSession());
            logger.info("导入机构入库耗时：" + (System.currentTimeMillis() - start) + "ms");
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
        if (checkParam(orgIds, getUserSession())) {
            long start = System.currentTimeMillis();
            organizationService.sortOrganization(orgIds);
            logger.info("机构排序耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 如果划转机构前后  单位发生了变化，则人员的单位要修改
     *
     * @param orgTransferBO
     * @return
     */
    @PostMapping("/transferOrganization")
    public ResponseResult transferOrganization(@RequestBody OrganizationTransferBO orgTransferBO) {
        //校验参数
        if (checkParam(orgTransferBO, getUserSession())) {
            long start = System.currentTimeMillis();
            organizationService.transferOrganization(orgTransferBO, getUserSession());
            logger.info("划转机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    //TODO
    @PostMapping("/mergeOrganization")
    public ResponseResult mergeOrganization(@RequestBody OrganizationMergeBO orgMergeBO) {
        //校验参数
        if (checkParam(orgMergeBO, getUserSession())) {
            long start = System.currentTimeMillis();
            //进行机构合并
            organizationService.mergeOrganization(orgMergeBO, getUserSession());
            logger.info("合并机构耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "根据orgId生成机构编码（用于新增机构时获取新机构编码）")
    @GetMapping("/generateOrgCode")
    public ResponseResult generateOrgCode(Integer orgId) {
        //校验参数
        if (checkParam(orgId, getUserSession())) {
            long start = System.currentTimeMillis();
            String orgCode = organizationService.generateOrgCode(orgId);
            logger.info("根据orgId生成机构编码耗时：" + (System.currentTimeMillis() - start) + "ms");
            return new ResponseResult<>(orgCode);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
}
