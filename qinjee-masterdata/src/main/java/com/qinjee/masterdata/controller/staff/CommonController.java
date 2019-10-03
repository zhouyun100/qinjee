package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
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
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】公用设计相关接口")
public class CommonController extends BaseController {
    @Autowired
    private IStaffCommonService staffCommonService;
    /**
     * 新增自定义表
     */
    @RequestMapping(value = "/insertArchiveCustomTable", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义表", notes = "hkt")
    @ApiImplicitParam(name = "CustomTable", value = "自定义表", paramType = "form", required = true)
    public ResponseResult insertCustomTable(CustomArchiveTable customArchiveTable) {
        return staffCommonService.insertCustomArichiveTable(customArchiveTable);
    }
    /**
     * 删除自定义表
     */
    @RequestMapping(value = "/deleteCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveTableId", value = "自定义表id组成集合", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveTable(List<Integer> list) {
      return staffCommonService.deleteCustomArchiveTable(list);
    }

    /**
     * 自定义表修改
     */
    @RequestMapping(value = "/updateCustomArchiveTable", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveTable", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveTable(CustomArchiveTable customArchiveTable) {
        return staffCommonService.updateCustomArchiveTable(customArchiveTable);
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
        return staffCommonService.selectCustomArchiveTable(currentPage,pageSize);
    }

    /**
     * 新增自定义组
     */
    @RequestMapping(value = "/insertCustomArchiveGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult insertCustomGroup(CustomArchiveGroup customArchiveGroup) {
        return staffCommonService.insertCustomArchiveGroup(customArchiveGroup);
    }

    /**
     * 删除自定义组
     */
    @RequestMapping(value = "/deleteCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义组", notes = "hkt")
    @ApiImplicitParam(name = "deleteCustomArchiveGroup", value = "自定义组id组成的集合", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveGroup(List<Integer> list) {
        return staffCommonService.deleteCustomArchiveGroup(list);
    }

    /**
     * 自定义组修改
     */
    @RequestMapping(value = "/updateCustomArchiveGroup", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义组", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult updateCustomGroup(CustomArchiveGroup customArchiveGroup) {
        return staffCommonService.updateCustomArchiveGroup(customArchiveGroup);
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
    public ResponseResult<PageResult<CustomArchiveTable>> selectTableFromGroup(Integer currentPage, Integer pageSize, Integer CustomArchiveGroupId) {
        return staffCommonService.selectCustomTableFromGroup(currentPage,pageSize,CustomArchiveGroupId);
    }
    /**
     * 新增自定义字段类型
     */

    @RequestMapping(value = "/insertCustomArchiveField", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult insertCustomField(CustomArchiveField customArchiveField) {
        return staffCommonService.insertCustomArchiveField(customArchiveField);
    }

    /**
     * 删除自定义字段
     */

    @RequestMapping(value = "/deleteCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomArchiveField(List<Integer> list) {
        return staffCommonService.deleteCustomArchiveField(list);
    }

    /**
     * 修改自定义字段类型
     */

    @RequestMapping(value = "/updateCustomArchiveField", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveField", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveField(CustomArchiveField customArchiveField) {
        return staffCommonService.updateCustomArchiveField(customArchiveField);
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
    public ResponseResult<PageResult<CustomArchiveField>> selectCustomArchiveField(Integer currentPage,Integer pageSize,Integer customArchiveTableId) {
        return staffCommonService.selectCustomArchiveField(currentPage,pageSize,customArchiveTableId);
    }
    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */
    @RequestMapping(value = "/selectCustomArchiveFieldById", method = RequestMethod.GET)
    @ApiOperation(value = "通过自定义字段id找到对应的自定义字段信息", notes = "hkt")
    @ApiImplicitParam(name = "customArchiveFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<CustomArchiveField> selectCustomArchiveFieldById(Integer customArchiveFieldId) {
        return staffCommonService.selectCustomArchiveFieldById(customArchiveFieldId);
    }

    /**
     * 将自定义字段信息存储到自定义表中
     * 这里还是需要参照数据库中自定义表数据里的字段内容
     */
    @RequestMapping(value = "/insertCustomArchiveTableData", method = RequestMethod.POST)
    @ApiOperation(value = "此接口需要将页面自定义表中用户填写的信息封装成一个jsonObject，然后传给后台拼接", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)
    public ResponseResult insertCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        return staffCommonService.insertCustomArchiveTableData(customArchiveTableData);
    }

    /**
     * 修改自定义字段表中的数据
     */

    @RequestMapping(value = "/updateCustomArchiveTableData", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义数据表中的记录", notes = "hkt")
    @ApiImplicitParam(name = "CustomArchiveTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult updateCustomArchiveTableData(CustomArchiveTableData customArchiveTableData) {
        return staffCommonService.updateCustomArchiveTableData(customArchiveTableData);
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
    public ResponseResult<PageResult<CustomArchiveTableData>> selectCustomTableData(Integer currentPage,Integer pageSize,Integer customArchiveTableId) {
        return staffCommonService.selectCustomArchiveTableData(currentPage,pageSize,customArchiveTableId);
    }


    /**
     * 获取字段校验类型
     */

    @RequestMapping(value = "/checkField ", method = RequestMethod.GET)
    @ApiOperation(value = "集合存储字段所需要的检验类型，考虑是否直接放在字段表中", notes = "hkt")
    @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "id", required = true)
    public ResponseResult<List<String>> checkField(Integer fieldId) {
        return staffCommonService.checkField(fieldId);
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
        return staffCommonService.exportFile(path,title,customArchiveTableDataId);
    }

    /**
     * 模板导入
     */
    @RequestMapping(value = "/importFile ", method = RequestMethod.POST)
    @ApiOperation(value = "模板导入", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)
    public ResponseResult<List<String[]>> importFile(String path) {

        return staffCommonService.importFile(path);
    }
    /**
     * 根据档案显示对应权限下的单位
     */
    @RequestMapping(value = "/getCompany ", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案显示对应权限下的单位", notes = "hkt")
    public ResponseResult getCompany() {
        Integer archiveId = userSession.getArchiveId();
        return staffCommonService.getCompany(archiveId);
    }
    /**
     * 根据档案id显示对应权限下的子集部门
     */
    @RequestMapping(value = "/getOrgIdByCompanyId ", method = RequestMethod.GET)
    @ApiOperation(value = "根据档案id显示对应权限下的子集部门", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult getOrgIdByCompanyId(Integer orgId) {
        return staffCommonService.getOrgIdByCompanyId(orgId);
    }
    /**
     * 显示部门下的岗位
     */
    @RequestMapping(value = "/getPostByOrgId ", method = RequestMethod.GET)
    @ApiOperation(value = "显示部门下的岗位", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "部门id", paramType = "query", required = true)
    public ResponseResult getPostByOrgId(Integer orgId) {
        return staffCommonService.getPostByOrgId(orgId);
    }
    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端简历文件然后确定上传位置
     */
    @RequestMapping(value = "/UploadFile ", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult UploadFile(String path) {

        return staffCommonService.putFile(path);
    }

    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端简历文件然后确定上传位置
     */
    @RequestMapping(value = "/UploadFileByForWard ", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult UploadFileByForWard() {

        return staffCommonService.UploadFileByForWard();
    }

}
