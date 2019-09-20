package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.model.entity.CustomField;
import com.qinjee.masterdata.model.entity.CustomGroup;
import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.masterdata.model.entity.CustomTableData;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
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
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】公用设计相关接口")
public class CommonController {
    @Autowired
    private IStaffCommonService staffCommonService;
    /**
     * 新增自定义表
     */
    @RequestMapping(value = "/insertCustomTable", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTable", value = "自定义表", paramType = "form", required = true)
    public ResponseResult insertCustomTable(CustomTable customTable) {
        return staffCommonService.insert(customTable);
    }
    /**
     * 删除自定义表
     */
    @RequestMapping(value = "/deleteCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTableId", value = "自定义表id组成集合", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult<Boolean> deleteCustomTable(List list) {
      return staffCommonService.deleteCustomTable(list);
    }

    /**
     * 自定义表修改
     */
    @RequestMapping(value = "/updateCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTable", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomTable(CustomTable customTable) {
        return staffCommonService.updateCustomTable(customTable);
    }

    /**
     * 展示自定义表
     */
    @RequestMapping(value = "/selectCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
    })
    public ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage, Integer pageSize) {
        return staffCommonService.selectCustomTable(currentPage,pageSize);
    }

    /**
     * 新增自定义组
     */
    @RequestMapping(value = "/insertCustomGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult insertCustomGroup(CustomGroup customGroup) {
        return staffCommonService.insertCustomGroup(customGroup);
    }

    /**
     * 删除自定义组
     */
    @RequestMapping(value = "/deleteCustomGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customGroupId", value = "自定义组id组成的数组", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult deleteCustomGroup(List<Integer> list) {
        return staffCommonService.deleteCustomGroup(list);
    }

    /**
     * 自定义组修改
     */
    @RequestMapping(value = "/updateCustomGroup", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult updateCustomGroup(CustomGroup customGroup) {
        return staffCommonService.updateCustomGroup(customGroup);
    }

    /**
     * 展示自定义组中的表
     */
    @RequestMapping(value = "/selectTableFromGroup", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义组中的表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "CustomGroupId", value = "当前自定义组的id", paramType = "query", required = true),
    })
    public ResponseResult<PageResult<CustomTable>> selectTableFromGroup(Integer currentPage, Integer pageSize, Integer customGroupId) {
        return staffCommonService.selectCustomTableFromGroup(currentPage,pageSize,customGroupId);
    }

    /**
     * 新增自定义字段类型
     */

    @RequestMapping(value = "/insertCustomField", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult insertCustomField(CustomField customField) {
        return staffCommonService.insertCustomField(customField);
    }

    /**
     * 删除自定义字段
     */

    @RequestMapping(value = "/deleteCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "list", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult deleteCustomField(List<Integer> list) {
        return staffCommonService.deleteCustomField(list);
    }

    /**
     * 修改自定义字段类型
     */

    @RequestMapping(value = "/updateCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customField", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult updateCustomField(CustomField customField) {
        return staffCommonService.updateCustomField(customField);
    }

    /**
     * 分页展示指定自定义表下的自定义字段
     */

    @RequestMapping(value = "/selectCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "分页展示指定自定义表下的自定义字段", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "customtableId", value = "自定义表id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<CustomField>> selectCustomField(Integer currentPage,Integer pageSize,Integer customTableId) {
        return staffCommonService.selectCustomFieldFromTable(currentPage,pageSize,customTableId);
    }
    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */
    @RequestMapping(value = "/selectCustomFieldById", method = RequestMethod.GET)
    @ApiOperation(value = "通过自定义字段id找到对应的自定义字段信息", notes = "hkt")
    @ApiImplicitParam(name = "customFieldId", value = "自定义字段id", paramType = "query", required = true)
    public ResponseResult<CustomField> selectCustomField(Integer customFieldId) {
        return staffCommonService.selectCustomFieldById(customFieldId);
    }
    /**
     * 通过自定义字段id找到对应的自定义字段信息
     */


    /**
     * 将自定义字段信息存储到自定义表中
     * 这里还是需要参照数据库中自定义表数据里的字段内容
     */
    @RequestMapping(value = "/insertCustomTableData", method = RequestMethod.POST)
    @ApiOperation(value = "此接口需要将页面自定义表中用户填写的信息封装成一个jsonObject，然后传给后台拼接", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)
    public ResponseResult insertCustomTableData(CustomTableData customTableData) {
        return staffCommonService.insertCustomTableData(customTableData);
    }

    /**
     * 修改自定义字段表中的数据
     */

    @RequestMapping(value = "/updateCustomTableData", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义数据表中的记录", notes = "hkt")
    @ApiImplicitParam(name = "customTableData", value = "自定义表数据信息", paramType = "form", required = true)
    public ResponseResult updateCustomTableData(CustomTableData customTableData) {
        return staffCommonService.updateCustomTableData(customTableData);
    }

    /**
     * 展示自定义表内容
     */

    @RequestMapping(value = "/selectCustomTableData", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表数据内容,返回自定义表数据", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "query", required = true),
            @ApiImplicitParam(name = "CustomTableDataId", value = "自定义数据表id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<CustomTableData>> selectCustomTableData(Integer currentPage,Integer pageSize,Integer customTableId) {
        return staffCommonService.selectCustomTableData(currentPage,pageSize,customTableId);
    }

    /**
     * 删除恢复,这个功能因为数据表中没有isdelete字段，所以删除就是真删除，更不用提删除恢复了
     */
    @RequestMapping(value = "/cancelDelete", method = RequestMethod.GET)
    @ApiOperation(value = "删除恢复,在删除预入职表，以及人员档案表时恢复", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true)
    public ResponseResult cancelDeleteCustom(Integer id) {
       return new ResponseResult(true,CommonCode.SUCCESS);
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
            @ApiImplicitParam(name = "customTableDataId", value = "自定义表数据id", paramType = "query", required = true),
    })
    public ResponseResult exportFile(String path,String title,Integer customTableId) {
        return staffCommonService.exportFile(path,title,customTableId);
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
