package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveGroup;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import com.qinjee.masterdata.model.vo.staff.ForWardPutFile;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.request.UserSession;
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
    @ApiImplicitParam(name = "CustomTable", value = "自定义表", paramType = "form", required = true)
    public ResponseResult insertCustomTable( @Valid CustomArchiveTable customArchiveTable) {
        Boolean b = checkParam(customArchiveTable);
        if(b){
            try {
                 staffCommonService.insertCustomArichiveTable(customArchiveTable);
                 return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义表失败");
            }

        }
        return  failResponseResult("自定义表参数错误");
    }

    /**
     * 删除自定义表
     */
    @RequestMapping(value = "/deleteCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveTableId", value = "自定义表id组成集合", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveTable(List<Integer> list) {
        Boolean b = checkParam(list);
        if(b){
            try {
                 staffCommonService.deleteCustomArchiveTable(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除自定义表失败");
            }
        }
        return  failResponseResult("自定义表id集合参数错误");
    }

    /**
     * 自定义表修改
     */
    @RequestMapping(value = "/updateCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveTable", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveTable( @Valid CustomArchiveTable customArchiveTable) {
        Boolean b = checkParam(customArchiveTable);
        if(b){
            try {
                staffCommonService.updateCustomArchiveTable(customArchiveTable);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改自定义表失败");
            }
        }
        return  failResponseResult("自定义表参数错误");
    }

    /**
     * 展示自定义表
     */
    @RequestMapping(value = "/selectCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
    })
    public ResponseResult<PageResult<CustomArchiveTable>> selectCustomTable(Integer currentPage, Integer pageSize) {
        Boolean b = checkParam(currentPage,pageSize);
        if(b){
            try {

                PageResult<CustomArchiveTable> pageResult = staffCommonService.selectCustomArchiveTable(currentPage, pageSize);
                return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("修改自定义表失败");
            }
        }
        return  failResponseResult("自定义表参数错误");

    }

    /**
     * 新增自定义组
     */
    @RequestMapping(value = "/insertCustomArchiveGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult insertCustomGroup( @Valid CustomArchiveGroup customArchiveGroup) {
        Boolean b = checkParam(customArchiveGroup);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveGroup(customArchiveGroup);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义组失败");
            }
        }
        return  failResponseResult("自定义组参数错误");
    }

    /**
     * 删除自定义组
     */
    @RequestMapping(value = "/deleteCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义组", notes = "hkt")
    @ApiImplicitParam(name = "deleteCustomArchiveGroup", value = "自定义组id组成的集合", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveGroup(List<Integer> list) {
        Boolean b = checkParam(list);
        if(b){
            try {
                staffCommonService.deleteCustomArchiveGroup(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除自定义组失败");
            }
        }
        return  failResponseResult("自定义组id集合参数错误");
    }

    /**
     * 自定义组修改
     */
    @RequestMapping(value = "/updateCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义组", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult updateCustomGroup( @Valid CustomArchiveGroup customArchiveGroup) {
        Boolean b = checkParam(customArchiveGroup);
        if(b){
            try {
                staffCommonService.updateCustomArchiveGroup(customArchiveGroup);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改自定义组失败");
            }
        }
        return  failResponseResult("自定义组参数错误");

    }

    /**
     * 展示自定义组中的表
     */
    @RequestMapping(value = "/selectTableFromGroup", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义组中的表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "CustomArchiveGroupId", value = "当前自定义组的id", paramType = "query", required = true),
    })
    public ResponseResult<PageResult<CustomArchiveTable>> selectTableFromGroup(Integer currentPage,
                                                                               Integer pageSize,
                                                                               Integer customArchiveGroupId) {
        Boolean b = checkParam(currentPage,pageSize,customArchiveGroupId);
        if(b){
            try {
                PageResult<CustomArchiveTable> pageResult = staffCommonService.selectCustomTableFromGroup(currentPage, pageSize, customArchiveGroupId);
                return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult(" 展示自定义表失败");
            }
        }
        return  failResponseResult("自定义组id参数错误");
    }
    /**
     *  展示企业下的自定义表名
     */
    @RequestMapping(value = "/selectTableFromOrg", method = RequestMethod.GET)
    @ApiOperation(value = "展示企业下的自定义表名", notes = "hkt")
    public ResponseResult<List<String>> selectTableFromGroup(Integer id) {
        Boolean b = checkParam(getUserSession());
        if(b){
            try {
                List<String> list=staffCommonService.selectTableFromOrg(getUserSession());
                if(CollectionUtils.isEmpty(list)){
                    return failResponseResult("没有相应的表名");
                }
                return new ResponseResult<>(list,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("展示企业下的自定义表名失败");
            }
        }
        return  failResponseResult("session错误");
    }

    /**
     * 新增自定义字段类型
     */

    @RequestMapping(value = "/insertCustomArchiveField", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult insertCustomField( @Valid CustomArchiveField customArchiveField) {
        Boolean b = checkParam(customArchiveField);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveField(customArchiveField);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增自定义字段失败");
            }
        }
        return  failResponseResult("自定义组参数错误");
    }

    /**
     * 删除自定义字段
     */

    @RequestMapping(value = "/deleteCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveField(List<Integer> list) {
        Boolean b = checkParam(list);
        if(b){
            try {
                staffCommonService.deleteCustomArchiveField(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("逻辑删除自定义字段失败");
            }
        }
        return  failResponseResult("自定义字段id集合参数错误");
    }

    /**
     * 修改自定义字段类型
     */

    @RequestMapping(value = "/updateCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveField", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveField( @Valid CustomArchiveField customArchiveField) {
        Boolean b = checkParam(customArchiveField);
        if(b){
            try {
                staffCommonService.updateCustomArchiveField(customArchiveField);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改自定义字段失败");
            }
        }
        return  failResponseResult("自定义字段参数错误");
    }

    /**
     * 分页展示指定自定义表下的自定义字段
     */

    @RequestMapping(value = "/selectCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "分页展示指定自定义表下的自定义字段", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "customArchivetableId", value = "自定义表id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<CustomArchiveField>> selectCustomArchiveField(Integer currentPage,Integer pageSize,
                                                                                   Integer customArchiveTableId) {
        Boolean b = checkParam(currentPage,pageSize,customArchiveTableId);
        if(b){
            try {

                PageResult<CustomArchiveField> pageResult =
                        staffCommonService.selectCustomArchiveField(currentPage, pageSize, customArchiveTableId);

                return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult(" 展示自定义字段失败");
            }
        }
        return  failResponseResult("自定义表id参数错误");

    }
    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */
    @RequestMapping(value = "/selectCustomArchiveFieldById", method = RequestMethod.GET)
    @ApiOperation(value = "通过自定义字段id找到对应的自定义字段信息", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if(b){
            try {
                CustomArchiveField customArchiveField = staffCommonService.selectCustomArchiveFieldById(customArchiveFieldId);
               return new ResponseResult<>(customArchiveField,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult(" 展示自定义字段失败");
            }
        }
        return  failResponseResult("自定义表id参数错误");
    }

    /**
     * 通过字段id找到对应的字段值
     */
    @RequestMapping(value = "/selectFieldValueById", method = RequestMethod.GET)
    @ApiOperation(value = "通过字段id找到对应的字段值", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<List<String>> selectFieldValueById(Integer customArchiveFieldId) {

        Boolean b = checkParam(customArchiveFieldId);
        if(b){
            try {
                List<String> strings=staffCommonService.staffCommonService(customArchiveFieldId);
                if(CollectionUtils.isEmpty(strings)){
                    return failResponseResult("字段值不存在");
                }
                return new ResponseResult<>(strings,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult(" 通过字段id找到对应的字段值失败");
            }
        }
        return  failResponseResult("自定义表id参数错误");
    }

    /**
     * 将自定义字段信息存储到自定义表中
     * 这里还是需要参照数据库中自定义表数据里的字段内容
     */
    @RequestMapping(value = "/insertCustomArchiveTableData", method = RequestMethod.POST)
    @ApiOperation(value = "此接口需要将页面自定义表中用户填写的信息封装成一个jsonObject，然后传给后台拼接", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)

    public ResponseResult insertCustomArchiveTableData( @Valid CustomArchiveTableData customArchiveTableData) {
        Boolean b = checkParam(customArchiveTableData);
        if (b) {
            try {
                staffCommonService.insertCustomArchiveTableData(customArchiveTableData,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("将数据存储到数据表失败");
            }
        }
        return  failResponseResult("自定义数据参数错误");
    }

    /**
     * 修改自定义字段表中的数据
     */

    @RequestMapping(value = "/updateCustomArchiveTableData", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义数据表中的记录", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveTableData( @Valid CustomArchiveTableData customArchiveTableData) {
        Boolean b = checkParam(customArchiveTableData);
        if(b){
            try {
                staffCommonService.updateCustomArchiveTableData(customArchiveTableData);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("修改自定义数据失败");
            }
        }
        return  failResponseResult("自定义数据参数错误");
    }
    /**
     * 展示自定义表内容
     */

    @RequestMapping(value = "/selectCustomArchiveTableData", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表数据内容,返回自定义表数据", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "CustomArchiveTableId", value = "自定义表id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<CustomArchiveTableData>> selectCustomTableData(Integer currentPage,Integer pageSize,
                                                                                    Integer customArchiveTableId) {
        Boolean b = checkParam(currentPage,pageSize,customArchiveTableId);
        if(b){
            try {
                PageResult<CustomArchiveTableData> pageResult =
                        staffCommonService.selectCustomArchiveTableData(currentPage, pageSize, customArchiveTableId);
                return new ResponseResult(pageResult,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult(" 展示自定义字段失败");
            }
        }
        return  failResponseResult("自定义表id参数错误");

    }


    /**
     * 获取字段校验类型
     */

    @RequestMapping(value = "/checkField ", method = RequestMethod.GET)
    @ApiOperation(value = "集合存储字段所需要的检验类型，考虑是否直接放在字段表中", notes = "hkt")
    @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "id", required = true)
    public ResponseResult<List<String>> checkField(Integer fieldId) {
        Boolean b = checkParam(fieldId);
        if(b){
            try {
                List<String> strings = staffCommonService.checkField(fieldId);
                return new ResponseResult(strings,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("获取字段校验类型失败");
            }
        }
        return  failResponseResult("字段id错误");
    }


    /**
     * 根据档案显示对应权限下的单位
     */
    @RequestMapping(value = "/getCompany ", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案显示对应权限下的单位", notes = "hkt")
    public ResponseResult getCompanyId() {
        Boolean b = checkParam(getUserSession());
        if(b) {
            try {
                List<Integer> companyId = staffCommonService.getCompanyId(getUserSession());
                return new ResponseResult(companyId, CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("显示单位id失败");
            }
        }
        return failResponseResult("session错误");
    }
    /**
     * 根据档案id显示对应权限下的子集部门
     */
    @RequestMapping(value = "/getOrgIdByCompanyId ", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案id显示对应权限下的子集部门", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult getOrgIdByCompanyId(Integer orgId) {
        Boolean b = checkParam(orgId);
        if(b){
            try {
                List<Integer> orgIdByCompanyId = staffCommonService.getOrgIdByCompanyId(orgId);
                return new ResponseResult(orgIdByCompanyId,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("根据档案id显示对应权限下的子集部门失败");
            }
        }
        return  failResponseResult("部门id错误");
    }
    /**
     * 显示部门下的岗位
     */
    @RequestMapping(value = "/getPostByOrgId ", method = RequestMethod.GET)
    @ApiOperation(value = "显示部门下的岗位", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult getPostByOrgId(Integer orgId) {
        Boolean b = checkParam(orgId);
        if(b){
            try {
                List<Integer> postByOrgId = staffCommonService.getPostByOrgId(orgId);
                return new ResponseResult(postByOrgId,CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("显示部门下的岗位失败");
            }
        }
        return  failResponseResult("部门id错误");

    }
    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端简历文件然后确定上传位置
     */
    @RequestMapping(value = "/UploadFile ", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult UploadFile(String path) {
        Boolean b = checkParam(path);
        if(b){
            try {
                staffCommonService.putFile(path);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("文件上传失败");
            }
        }
        return  failResponseResult("path错误");
    }

    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端简历文件然后确定上传位置
     */
    @RequestMapping(value = "/UploadFileByForWard ", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult<ForWardPutFile> UploadFileByForWard() {
            try {
                staffCommonService.UploadFileByForWard();
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("前端获取对象文件上传失败");
            }

    }
    /**
     * 模板导出
     */
    @RequestMapping(value = "/exporttFile ", method = RequestMethod.GET)
    @ApiOperation(value = "导出模板", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
            @ApiImplicitParam(name = "title", value = "excel标题", paramType = "query", required = true),
            @ApiImplicitParam(name = "customArchiveTableDataId", value = "自定义表数据id", paramType = "query", required = true),
    })
    public ResponseResult exportFile(String path,String title,Integer customArchiveTableDataId) {
        Boolean b = checkParam(path,title,customArchiveTableDataId);
        if(b){
            try {
                staffCommonService.exportFile(path,title,customArchiveTableDataId);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("导出失败");
            }
        }
        return  failResponseResult("参数错误");

    }

    /**
     * 模板导入
     */
    @RequestMapping(value = "/importFile ", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)
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
     * 检验参数
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
     * @param message
     * @return
     */
    public ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }
}
