package com.qinjee.masterdata.controller.organization;

import com.alibaba.fastjson.JSONArray;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.page.OrganizationPageVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.utils.pexcel.ExcelExportUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

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

  //TODO 新增子机构时需要维护 子机构与角色之间的联系
  //TODO 新增没有父机构的机构时  机构编码递增
  @GetMapping("/addOrganization")
  @ApiOperation(value = "ok，新增机构", notes = "ok")
  public ResponseResult addOrganization(@RequestParam("orgName") String orgName, @RequestParam("orgType") String orgType, @RequestParam(value = "orgParentId") String orgParentId, @RequestParam("orgManagerId") String orgManagerId) {
    return organizationService.addOrganization(orgName, orgType, orgParentId, orgManagerId, getUserSession());
  }

  @GetMapping("/editOrganization")
  @ApiOperation(value = "ok，编辑机构", notes = "机构编码待验证")
  public ResponseResult editOrganization(@RequestParam("orgCode") String orgCode, @RequestParam("orgId") String orgId, @RequestParam("orgName") String orgName, @RequestParam("orgType") String orgType, @RequestParam("orgParentId") String orgParentId, @RequestParam("orgManagerId") String orgManagerId) {
    return organizationService.editOrganization(orgCode, orgId, orgName, orgType, orgParentId, orgManagerId, getUserSession());
  }

  //TODO 删除机构时，需要回收权限（调用权限接口）
  @PostMapping("/deleteOrganizationById")
  @ApiOperation(value = "ok，删除机构", notes = "ok")
  public ResponseResult deleteOrganizationById(@ApiParam(value = "机构id列表") @RequestBody List<Integer> orgIds) {
    return organizationService.deleteOrganizationById(orgIds, getUserSession());
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
  public ResponseResult<PageResult<OrganizationVO>> getAllOrganizationTree(@RequestParam(value = "isEnable", required = false) Short isEnable) {
    if (isEnable == null || isEnable == 0) {
      isEnable = 0;
    } else {
      isEnable = null;
    }
    UserSession userSession = getUserSession();
    List<OrganizationVO> organizationVOList = organizationService.getAllOrganizationTree(userSession, isEnable);
    PageResult<OrganizationVO> pageResult = new PageResult<>(organizationVOList);
    return new ResponseResult<>(pageResult);
  }

  @PostMapping("/getOrganizationPageList")
  @ApiOperation(value = "ok，分页按条件查询用户下所有的机构(包含子孙)", notes = "ok")
  public ResponseResult<PageResult<OrganizationVO>> getOrganizationPageList(@RequestBody OrganizationPageVo organizationPageVo) {
    Short isEnable = organizationPageVo.getIsEnable();
    if (isEnable == null || isEnable == 0) {
      isEnable = 0;
    } else {
      isEnable = null;
    }
    organizationPageVo.setIsEnable(isEnable);
    UserSession userSession = getUserSession();
    PageResult<OrganizationVO> pageResult = organizationService.getAllOrganizationPageList(organizationPageVo, userSession);
    return new ResponseResult<>(pageResult);
  }

  @PostMapping("/getDirectOrganizationPageList")
  @ApiOperation(value = "ok，分页查询下级直属机构", notes = "ok")
  public ResponseResult<PageResult<OrganizationVO>> getDirectOrganizationPageList(@RequestBody OrganizationPageVo organizationPageVo) {
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
    if (isEnable == null || isEnable == 0) {
      isEnable = 0;
    } else {
      isEnable = null;
    }
    List<OrganizationVO> pageResult = organizationService.getOrganizationGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, orgId, isEnable);
    return new ResponseResult(pageResult);
  }

  //TODO
  @ApiOperation(value = "ok，获取机构岗位树", notes = "ok")
  @GetMapping("/getOrganizationPostTree")
  public ResponseResult<List<OrganizationVO>> getOrganizationPostTree(@ApiParam(value = "是否不包含封存：0包含（默认）、1 不包含", example = "0") @RequestParam("isEnable") Short isEnable) {
    if (isEnable == null) {
      isEnable = Short.parseShort("0");
    }

    return organizationService.getOrganizationPostTree(getUserSession(), isEnable);
  }

  @PostMapping("/lockOrganizationByIds")
  @ApiOperation(value = "ok，封存机构，设为0", notes = "ok")
  public ResponseResult lockOrganizationByIds(@RequestBody List<Integer> orgIds) {
    return organizationService.sealOrganizationByIds(orgIds, Short.parseShort("0"));
  }

  @PostMapping("/unlockOrganizationByIds")
  @ApiOperation(value = "ok，解封机构，设为1", notes = "ok")
  public ResponseResult unlockOrganizationByIds(@RequestBody List<Integer> orgIds) {
    return organizationService.sealOrganizationByIds(orgIds, Short.parseShort("1"));
  }
  // String filePath,  List<Integer> orgIds
  @PostMapping("/exportOrganization")
  public ResponseResult exportOrganization(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {
    ResponseResult responseResult = new ResponseResult();
    List<Integer> orgIds = null;
    Integer orgId = null;
    if (paramMap.get("orgIds") != null && paramMap.get("orgIds") instanceof List) {
      orgIds = (List<Integer>) paramMap.get("orgIds");
    }
    if (paramMap.get("orgId") != null && paramMap.get("orgId") instanceof Integer) {
      orgId = (Integer) paramMap.get("orgId");
    }
    try {
      List<OrganizationVO> organizationVOList = organizationService.exportOrganization(orgId, orgIds, getUserSession().getArchiveId());
      if (!CollectionUtils.isEmpty(organizationVOList)){
        byte[] bytes = ExcelExportUtil.exportToBytes(organizationVOList);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("fileName", URLEncoder.encode("orgDefualt.xls", "UTF-8"));
        response.setHeader("Content-Disposition",
            "attachment;filename=\"" + URLEncoder.encode("defualt", "UTF-8") + "\"");
        response.getOutputStream().write(bytes);
        return null;
      }else{
        responseResult.setMessage("机构为空");
      }

    } catch (Exception e) {
      responseResult.setResultCode(CommonCode.FILE_EXPORT_FAILED);
    }
    return responseResult;
  }


  @PostMapping("/uploadAndCheck")
  @ApiOperation(value = "ok,导入机构excel并校验", notes = "ok")
  public ResponseResult uploadAndCheck(MultipartFile multfile,HttpServletResponse response) throws Exception {
   return organizationService.uploadAndCheck(multfile,getUserSession(),response);

  }
  @PostMapping("/exportError2Txt")
  @ApiOperation(value = "ok,导出错误信息到txt", notes = "ok")
  public ResponseResult exportError2Txt(String redisKey,HttpServletResponse response) throws Exception {
    String errorData = redisClusterService.get(redisKey);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/x-msdownload;charset=UTF-8");
    response.setHeader("Content-Disposition",
        "attachment;filename=\"" + URLEncoder.encode("errorInfo", "UTF-8") + "\"");
    response.getOutputStream().write(errorData.getBytes());
    return null;
  }

  @GetMapping("/importToDatabase")
  @ApiOperation(value = "导入机构入库")
  public ResponseResult importToDatabase(@RequestParam("orgExcelRedisKey") String orgExcelRedisKey){
    return organizationService.importToDatabase(orgExcelRedisKey,getUserSession());
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
    ResponseResult responseResult = organizationService.sortOrganization(orgIds);
    return responseResult;
  }

  //TODO
  @GetMapping("/getUserArchiveListByUserName")
  @ApiOperation(value = "未实现，机构负责人查询，如果带负责人姓名，则根据姓名模糊查询，不带参则全量查询", notes = "需要调用人员接口")
  public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(@ApiParam(value = "姓名", example = "张三", required = true) @RequestParam(value = "userName", required = false) String userName) {
    return organizationService.getUserArchiveListByUserName(userName);
  }


  @ApiOperation(value = "ok，划转机构,参数demo  {\"orgIds\":[1001,1002],\"targetOrgId\":1003}")
  @PostMapping("/transferOrganization2")
  //TODO 还需要将人员进行划转
  public ResponseResult transferOrganization2(@RequestBody Map<String, Object> paramMap) {
    List<Integer> orgIds = (List<Integer>) paramMap.get("orgIds");
    Integer targetOrgId = (Integer) paramMap.get("targetOrgId");
    return organizationService.transferOrganization(orgIds, targetOrgId, getUserSession());
  }

  //TODO
  @PostMapping("/mergeOrganization")
  @ApiOperation(value = "ok，合并机构,传参demo  {\"newOrgName\":\"新机构名称\",\"orgIds\":[1001,1002],\"parentOrgId\":1003}", notes = "调通，需维护人员")
  public ResponseResult mergeOrganization(@RequestBody Map<String, Object> paramMap) {
    UserSession userSession = getUserSession();
    List<Integer> orgIds = (List<Integer>) paramMap.get("orgIds");
    Integer parentOrgId = (Integer) paramMap.get("parentOrgId");
    String newOrgName = (String) paramMap.get("newOrgName");
    return organizationService.mergeOrganization(newOrgName, parentOrgId, orgIds, userSession);
  }

  @ApiOperation(value = "ok，下载模板")
  @PostMapping("/downloadTemplate")
  public ResponseResult downloadTemplateInOrg(HttpServletResponse response) {
    String fileUrl="http://193.112.188.180/file/importTemplate/organization/机构导入模板.xls";
    ResponseResult responseResult=new ResponseResult();
    responseResult.setResult(fileUrl);
    return responseResult;
  }
}
