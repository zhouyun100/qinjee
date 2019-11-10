package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.staff.AttachmentVo;
import com.qinjee.masterdata.model.vo.staff.GetFilePath;
import com.qinjee.masterdata.model.vo.staff.export.ExportArc;
import com.qinjee.masterdata.model.vo.staff.export.ExportBusiness;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】公用设计相关接口")
public class CommonController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private IStaffCommonService staffCommonService;

    /**
     * 新增自定义表
     */
    @RequestMapping(value = "/insertArchiveCustomTable", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义表", paramType = "form" ,required = true)
    public ResponseResult insertCustomTable(@Valid CustomArchiveTable customArchiveTable, UserSession userSession) {
        Boolean b = checkParam(customArchiveTable, userSession);
        if (b) {
            try {
                staffCommonService.insertCustomArichiveTable(customArchiveTable, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义表失败");
            }

        }
        return failResponseResult("自定义表参数错误或者session错误");
    }

    /**
     * 删除自定义表
     */
    @RequestMapping(value = "/deleteCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义表id组成集合", paramType = "query", example = "{1,2}" ,allowMultiple = true)
    public ResponseResult deleteCustomArchiveTable(@RequestParam List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveTable(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除自定义表失败");
            }
        }
        return failResponseResult("自定义表id集合参数错误");
    }

    /**
     * 自定义表修改
     */
    @RequestMapping(value = "/updateCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义字段表", paramType = "form" )
    public ResponseResult updateCustomArchiveTable(@Valid CustomArchiveTable customArchiveTable) {
        Boolean b = checkParam(customArchiveTable);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveTable(customArchiveTable);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义表失败");
            }
        }
        return failResponseResult("自定义表参数错误");
    }

    /**
     * 展示自定义表
     */
    @RequestMapping(value = "/selectCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true),
//    })
    public ResponseResult<PageResult<CustomArchiveTable>> selectCustomTable(Integer currentPage, Integer pageSize) {
        Boolean b = checkParam(currentPage, pageSize, getUserSession());
        if (b) {
            try {
                PageResult<CustomArchiveTable> pageResult = staffCommonService.selectCustomArchiveTable(currentPage, pageSize, getUserSession());
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    /**
     * 新增自定义组
     */
    @RequestMapping(value = "/insertCustomArchiveGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult insertCustomGroup(@Valid CustomArchiveGroup customArchiveGroup, UserSession userSession) {
        Boolean b = checkParam(customArchiveGroup, userSession);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveGroup(customArchiveGroup, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义组失败");
            }
        }
        return failResponseResult("自定义组参数错误或session错误");
    }

    /**
     * 删除自定义组
     */
    @RequestMapping(value = "/deleteCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义组id组成的集合", paramType = "form", allowMultiple = true,example = "{1,2}")
    public ResponseResult deleteCustomArchiveGroup(@RequestParam List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveGroup(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
    }

    /**
     * 自定义组修改
     */
    @RequestMapping(value = "/updateCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义组", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult updateCustomGroup(@Valid CustomArchiveGroup customArchiveGroup) {
        Boolean b = checkParam(customArchiveGroup);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveGroup(customArchiveGroup);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义组失败");
            }
        }
        return failResponseResult("自定义组参数错误");

    }

    /**
     * 展示自定义组中的字段
     */
    @RequestMapping(value = "/selectArchiveFieldFromGroup", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义组中的表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "customArchiveGroupId", value = "当前自定义组的id", paramType = "query", required = true),
//    })
    public ResponseResult<PageResult<CustomArchiveField>> selectArchiveFieldFromGroup(Integer currentPage,
                                                                                      Integer pageSize,
                                                                                      Integer customArchiveGroupId) {
        Boolean b = checkParam(currentPage, pageSize, customArchiveGroupId);
        if (b) {
            try {
                PageResult<CustomArchiveField> pageResult =
                        staffCommonService.selectArchiveFieldFromGroup(currentPage, pageSize, customArchiveGroupId);
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 展示企业下自定义表名
     */
    @RequestMapping(value = "/selectTableFromOrg", method = RequestMethod.GET)
    @ApiOperation(value = "展示企业下的自定义表名", notes = "hkt")
    public ResponseResult<List<String>> selectTableFromGroup() {
        Boolean b = checkParam(getUserSession());
        if (b) {
            try {
                List<String> list = staffCommonService.selectTableFromOrg(getUserSession());
                if (CollectionUtils.isEmpty(list)) {
                    return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
                }
                return new ResponseResult<>(list, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 新增自定义字段类型
     */

    @RequestMapping(value = "/insertCustomArchiveField", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult insertCustomField(@Valid CustomArchiveField customArchiveField, UserSession userSession) {
        Boolean b = checkParam(customArchiveField, userSession);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveField(customArchiveField, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义字段失败");
            }
        }
        return failResponseResult("自定义组参数错误");
    }

    /**
     * 删除自定义字段
     */

    @RequestMapping(value = "/deleteCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveField(@RequestParam List<Integer> list) {
        Boolean b = checkParam(list);
        if (b) {
            try {
                staffCommonService.deleteCustomArchiveField(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
    }

    /**
     * 修改自定义字段类型
     */

    @RequestMapping(value = "/updateCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义字段", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveField", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveField(@Valid CustomArchiveField customArchiveField) {
        Boolean b = checkParam(customArchiveField);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveField(customArchiveField);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义字段失败");
            }
        }
        return failResponseResult("自定义字段参数错误");
    }

    /**
     * 分页展示指定自定义表下的自定义字段
     */

    @RequestMapping(value = "/selectCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "分页展示指定自定义表下的自定义字段", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "customArchivetableId", value = "自定义表id", paramType = "query", required = true)
//    })
    public ResponseResult<PageResult<CustomArchiveField>> selectCustomArchiveField(Integer currentPage, Integer pageSize,
                                                                                   Integer customArchiveTableId) {
        Boolean b = checkParam(currentPage, pageSize, customArchiveTableId);
        if (b) {
            try {

                PageResult<CustomArchiveField> pageResult =
                        staffCommonService.selectCustomArchiveField(currentPage, pageSize, customArchiveTableId);
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */
    @RequestMapping(value = "/selectCustomArchiveFieldById", method = RequestMethod.GET)
    @ApiOperation(value = "通过自定义字段id找到对应的自定义字段信息", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if (b) {
            try {
                CustomArchiveField customArchiveField = staffCommonService.selectCustomArchiveFieldById(customArchiveFieldId);
                if (customArchiveField != null) {

                    return new ResponseResult<>(customArchiveField, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 通过字段id找到对应的字段值
     */
    @RequestMapping(value = "/selectFieldValueById", method = RequestMethod.GET)
    @ApiOperation(value = "通过字段id找到对应的字段值", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<List<String>> selectFieldValueById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if (b) {
            try {
                List<String> strings = staffCommonService.selectFieldValueById(customArchiveFieldId);
                if (CollectionUtils.isEmpty(strings)) {
                    return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
                }
                return new ResponseResult<>(strings, CommonCode.SUCCESS);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 将自定义字段信息存储到自定义表中
     * 这里还是需要参照数据库中自定义表数据里的字段内容
     */
    @RequestMapping(value = "/insertCustomArchiveTableData", method = RequestMethod.POST)
    @ApiOperation(value = "将自定义字段信息存储到自定义表中,需要将自定义表中的值封装成jsonObject形式传到后端，然后传给后台拼接", notes = "hkt")
//    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)

    public ResponseResult insertCustomArchiveTableData(@Valid CustomArchiveTableData customArchiveTableData) {
        Boolean b = checkParam(customArchiveTableData);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveTableData(customArchiveTableData, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("将数据存储到数据表失败");
            }
        }
        return failResponseResult("自定义数据参数错误");
    }

    /**
     * 修改自定义字段表中的数据
     */
    @RequestMapping(value = "/updateCustomArchiveTableData", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义数据表中的记录", notes = "hkt")
//    @ApiImplicitParam(name = "CustomArchiveTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveTableData(@Valid CustomArchiveTableData customArchiveTableData) {
        Boolean b = checkParam(customArchiveTableData);
        if (b) {
            try {
                staffCommonService.updateCustomArchiveTableData(customArchiveTableData);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改自定义数据失败");
            }
        }
        return failResponseResult("自定义数据参数错误");
    }

    /**
     * 展示自定义表内容
     */
    @RequestMapping(value = "/selectCustomArchiveTableData", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表数据内容,返回自定义表数据", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
//            @ApiImplicitParam(name = "CustomArchiveTableId", value = "自定义表id", paramType = "query", required = true)
//    })
    public ResponseResult<PageResult<CustomArchiveTableData>> selectCustomTableData(Integer currentPage, Integer pageSize,
                                                                                    Integer customArchiveTableId) {
        Boolean b = checkParam(currentPage, pageSize, customArchiveTableId);
        if (b) {
            try {
                PageResult<CustomArchiveTableData> pageResult =
                        staffCommonService.selectCustomArchiveTableData(currentPage, pageSize, customArchiveTableId);
                if (!CollectionUtils.isEmpty(pageResult.getList())) {
                    return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(pageResult, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    /**
     * 获取字段校验类型
     */

    @RequestMapping(value = "/checkField", method = RequestMethod.GET)
    @ApiOperation(value = "集合存储字段所需要的检验类型，考虑是否直接放在字段表中", notes = "hkt")
//    @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "id", required = true)
    public ResponseResult<List<String>> checkField(Integer fieldId) {
        Boolean b = checkParam(fieldId);
        if (b) {
            try {
                List<String> strings = staffCommonService.checkField(fieldId);
                if (!CollectionUtils.isEmpty(strings)) {
                    return new ResponseResult<>(strings, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            }
        }
        return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
    }


    /**
     * 根据档案显示对应权限下的单位
     * swagger参数不允许为空，test是伪造数据
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案显示对应权限下的单位", notes = "hkt")
    public ResponseResult<List<Integer>> getCompanyId() {
        Boolean b = checkParam(getUserSession());
        if (b) {
            try {
                List<Integer> companyId = staffCommonService.getCompanyId(getUserSession());
                if (!CollectionUtils.isEmpty(companyId)) {
                    return new ResponseResult<>(companyId, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    /**
     * 根据档案id显示对应权限下的子集部门
     */
    @RequestMapping(value = "/getOrgIdByCompanyId", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案id显示对应权限下的子集部门", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult getOrgIdByCompanyId(Integer orgId) {
        Boolean b = checkParam(orgId);
        if (b) {
            try {
                List<Integer> orgIdByCompanyId = staffCommonService.getOrgIdByCompanyId(orgId);
                if (!CollectionUtils.isEmpty(orgIdByCompanyId)) {
                    return new ResponseResult<>(orgIdByCompanyId, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("根据档案id显示对应权限下的子集部门失败");
            }
        }
        return failResponseResult("部门id错误");
    }

    /**
     * 显示部门下的岗位
     */
    @RequestMapping(value = "/getPostByOrgId", method = RequestMethod.GET)
    @ApiOperation(value = "显示部门下的岗位", notes = "hkt")
//    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult<List<Post>> getPostByOrgId(Integer orgId) {
        Boolean b = checkParam(orgId);
        if (b) {
            try {
                List<Post> postByOrgId = staffCommonService.getPostByOrgId(orgId);
                if (!CollectionUtils.isEmpty(postByOrgId)) {
                    return new ResponseResult<>(postByOrgId, CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null, CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端建立文件然后确定上传位置
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)
    public ResponseResult uploadFile(MultipartFile multipartFile,@RequestBody @Valid AttachmentVo attachmentVo) {
        Boolean b = checkParam(multipartFile,attachmentVo,getUserSession());
        if (b) {
            try {
                staffCommonService.putFile(multipartFile,attachmentVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("文件上传失败");
            }
        }
        return failResponseResult("path错误");
    }
    /**
     * 获得文件路径
     */
    @RequestMapping(value = "/getFilePath", method = RequestMethod.POST)
    @ApiOperation(value = "获得文件路径", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)
    public ResponseResult<List<String>> getFilePath(@Valid @RequestBody GetFilePath getFilePath) {
        Boolean b = checkParam(getFilePath,userSession);
        if (b) {
            try {
                List<String> list=staffCommonService.getFilePath(getFilePath,getUserSession());
                if(list.size()>0){
                    return new ResponseResult<>(list,CommonCode.SUCCESS);
                }else{
                    return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 文件下载
     */
    @RequestMapping(value = "/downLoadFile", method = RequestMethod.POST)
    @ApiOperation(value = "文件下载", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult downLoadFile(String path) {
        Boolean b = checkParam(path);
        if(b) {
            try {
                staffCommonService.downLoadFile(path);
               return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("下载文件失败！");
            }
        }
        return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
    }

    /**
     * 模板导出档案
     */
    @RequestMapping(value = "/exportArcFile", method = RequestMethod.POST)
    @ApiOperation(value = "导出档案模板", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "QuerySchemeId", value = "查询方案id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "人员id集合", paramType = "query", required = true),
//    })
    //导出的文件应该是以.xls结尾
    public ResponseResult exportArcFile( @Valid @RequestBody ExportArc exportArc,
                                         HttpServletResponse response) {
        Boolean b = checkParam(exportArc,getUserSession(),response);
        if(b){
            try {
                staffCommonService.exportArcFile(exportArc,response,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("导出失败");
            }
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 模板导入档案
     */
    @RequestMapping(value = "/importFile", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入", notes = "hkt")
//    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)

    public ResponseResult importFile(String path) {
        Boolean b = checkParam(path,getUserSession());
        if(b){
            try {
                staffCommonService.importFile(path,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("导入失败");
            }
        }
        return  failResponseResult("path错误");
    }


    /**
     * 模板导出预入职
     */
    @RequestMapping(value = "/exportPreFile", method = RequestMethod.GET)
    @ApiOperation(value = "导出预入职模板", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportPreFile(@Valid ExportArc exportArc, HttpServletResponse response) {
        Boolean b = checkParam(exportArc,response,getUserSession());
        if(b){
            try {
                staffCommonService.exportPreFile(exportArc,response,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("导出失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 模板导出业务类
     */
    @RequestMapping(value = "/exportBusiness", method = RequestMethod.GET)
    @ApiOperation(value = "模板导出业务类", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
//            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true),
//    })

    public ResponseResult exportBusiness(@Valid ExportBusiness exportBusiness, HttpServletResponse response) {
        Boolean b = checkParam(exportBusiness,response,getUserSession());
        if(b){
            try {
                staffCommonService.exportBusiness(exportBusiness,response,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("导出失败");
            }
        }
        return  failResponseResult("参数错误");
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
