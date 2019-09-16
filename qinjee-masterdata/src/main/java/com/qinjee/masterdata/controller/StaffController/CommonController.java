package com.qinjee.masterdata.controller.StaffController;

import com.alibaba.fastjson.JSONObject;
import com.qinjee.masterdata.dao.CustomTableDao;
import com.qinjee.masterdata.model.entity.CustomField;
import com.qinjee.masterdata.model.entity.CustomGroup;
import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.masterdata.model.entity.CustomTableData;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】公用设计相关接口")
public class CommonController {
    @Autowired
    private CustomTableDao customTableDao;
    /**
     * 新增自定义表
     */
    @RequestMapping(value = "/insertCustomTable", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTable", value = "自定义表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertCustomTable(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            int insert = customTableDao.insert(customTable);
            if (insert > 0) {
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            } else {
                return new ResponseResult<>(false, CommonCode.FAIL);
            }
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }

    }

    /**
     * 删除自定义表
     */
    @RequestMapping(value = "/deleteCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTableId", value = "自定义表id组成集合", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult<Boolean> deleteCustomTable(List list) {
        int max = customTableDao.selectMaxPrimaryKey();
        for (int i = 0; i < list.size(); i++) {
           if(!(list.get(i) instanceof Integer) ){
              return new ResponseResult<>(false,CommonCode.INVALID_PARAM);
           }
           if((Integer)list.get(i) > max){
               return new ResponseResult<>(false,CommonCode.INVALID_PARAM);
           }
            int delete = customTableDao.PretenddeleteByPrimaryKey((Integer) list.get(i));
           if(delete<0){
               return  new ResponseResult<>(false,CommonCode.FAIL);
           }
        }
        return   new ResponseResult<>(true, CommonCode.SUCCESS);
    }

    /**
     * 自定义表修改
     */
    @RequestMapping(value = "/updateCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义表", notes = "hkt")
    @ApiImplicitParam(name = "customTable", value = "自定义字段表", paramType = "form", required = true)
    public ResponseResult<Boolean> updateCustomTable(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            int i = customTableDao.updateByPrimaryKey(customTable);
            if (i > 0) {
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            } else {
                return new ResponseResult<>(false, CommonCode.FAIL);
            }
        }
        return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
    }

    /**
     * 展示自定义表
     */
    @RequestMapping(value = "/selectCustomTable", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
    })
    public ResponseResult<PageResult<List<CustomTable>>> selectCustomTable(Integer number, Integer pageSize) {
        PageResult<List<CustomTable>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<CustomTable>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 新增自定义组
     */
    @RequestMapping(value = "/insertCustomGroup", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义租", notes = "hkt")
    @ApiImplicitParam(name = "customGroup", value = "自定义组", paramType = "form", required = true)
    public ResponseResult<Boolean> insertCustomGroup(CustomGroup customGroup) {
        ResponseResult<Boolean> insertResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return insertResponseResult;
    }

    /**
     * 删除自定义组
     */
    @RequestMapping(value = "/deleteCustomGroup", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义组", notes = "hkt")
    @ApiImplicitParam(name = "customGroupId", value = "自定义组id组成的数组", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult<Boolean> deleteCustomGroup(List<Integer> id) {
        ResponseResult<Boolean> deleteResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return deleteResponseResult;
    }

    /**
     * 自定义组修改
     */
    @RequestMapping(value = "/updateCustomGroup", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义组", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customGroupId", value = "自定义组id", paramType = "query", required = true),
            @ApiImplicitParam(name = "customGroup", value = "自定义组", paramType = "form", required = true)
    })
    public ResponseResult<Boolean> updateCustomGroup(Integer id, CustomGroup customGroup) {
        ResponseResult<Boolean> updateResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return updateResponseResult;
    }

    /**
     * 展示自定义组中的表
     */
    @RequestMapping(value = "/selectTableFromGroup", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义组中的表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
            @ApiImplicitParam(name = "CustomGroupId", value = "当前自定义组的id", paramType = "form", required = true),
    })
    public ResponseResult<PageResult<List<CustomTable>>> selectTableFromGroup(Integer number, Integer pageSize, Integer CustomGroupId) {
        PageResult<List<CustomTable>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<CustomTable>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 新增自定义字段类型
     */

    @RequestMapping(value = "/insertCustomField", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customField", value = "自定义字段对象", paramType = "form", required = true)
    public ResponseResult<Boolean> insertCustomField(CustomField customField) {
        ResponseResult<Boolean> insertResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return insertResponseResult;
    }

    /**
     * 删除自定义字段类型
     */

    @RequestMapping(value = "/deleteCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义字段", notes = "hkt")
    @ApiImplicitParam(name = "customFieldId", value = "自定义字段id", paramType = "query", required = true, example = "{1,2}")
    public ResponseResult<Boolean> deleteCustomField(Integer[] id) {
        ResponseResult<Boolean> integerResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return integerResponseResult;
    }

    /**
     * 修改自定义字段类型
     */

    @RequestMapping(value = "/updateCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义字段", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customFieldId", value = "自定义字段表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "customField", value = "自定义字段表", paramType = "form", required = true)
    })
    public ResponseResult<Boolean> updateCustomField(Integer id, CustomField customField) {
        ResponseResult<Boolean> insertResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return insertResponseResult;
    }

    /**
     * 展示自定义字段类型
     */

    @RequestMapping(value = "/selectCustomField", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义字段", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
            @ApiImplicitParam(name = "customtableId", value = "自定义表id", paramType = "query", required = true)
    })

    public ResponseResult<PageResult<List<CustomField>>> selectCustomField(Integer id) {
        PageResult<List<CustomField>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<CustomField>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 将自定义字段信息存储到自定义表中
     */


    @RequestMapping(value = "/insertCustomTableData", method = RequestMethod.POST)
    @ApiOperation(value = "此接口需要将页面自定义表中用户填写的信息封装成一个jsonObject，然后传给后台拼接", notes = "hkt")
    @ApiImplicitParam(name = "JsonObject", value = "用户所填写的信息与操作人信息,处理之后存入自定义表数据", paramType = "form", required = true)
    public ResponseResult<Boolean> insertCustomTableData(JSONObject jsonObject) {
        ResponseResult<Boolean> customTableDataResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return customTableDataResponseResult;
    }

    /**
     * 删除自定义数据表中的记录
     */

    @RequestMapping(value = "/deleteCustomTableData", method = RequestMethod.GET)
    @ApiOperation(value = "删除自定义数据表中的记录", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "自定义数据表的id", paramType = "form", required = true, example = "{1,2}")
    public ResponseResult<Boolean> deleteCustomTableData(Integer[] id) {
        ResponseResult<Boolean> customTableDataResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return customTableDataResponseResult;
    }

    /**
     * 修改自定义字段表中的数据
     */

    @RequestMapping(value = "/updateCustomTableData", method = RequestMethod.GET)
    @ApiOperation(value = "修改自定义数据表中的记录", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customTableDataId", value = "自定义数据表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "jsonObject", value = "页面内容更改过后形成的jsonObject以及其它白哦是信息形成的jsonObject", paramType = "form", required = true)
    })
    public ResponseResult<Boolean> updateCustomTableData(Integer id, CustomTableData customTableData) {
        ResponseResult<Boolean> updateResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return updateResponseResult;
    }

    /**
     * 展示自定义表内容
     */

    @RequestMapping(value = "/selectCustomTableData", method = RequestMethod.GET)
    @ApiOperation(value = "展示自定义表数据内容,返回需要展示的Json", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
            @ApiImplicitParam(name = "CustomTableDataId", value = "自定义数据表id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<List<CustomTableData>>> selectCustomTableData(Integer id) {
        PageResult<List<CustomTableData>> listPageResult = new PageResult<>();
        ResponseResult<PageResult<List<CustomTableData>>> pageResultResponseResult = new ResponseResult<>(listPageResult, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }

    /**
     * 删除恢复
     */


    @RequestMapping(value = "/cancelDelete", method = RequestMethod.GET)
    @ApiOperation(value = "删除恢复,在删除预入职表，以及人员档案表时恢复", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true)
    public ResponseResult<Boolean> cancelDelete(Integer id) {
        ResponseResult<Boolean> cancelDeleteResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return cancelDeleteResponseResult;
    }

    /**
     * 获取字段校验类型
     */

    @RequestMapping(value = "/checkField ", method = RequestMethod.GET)
    @ApiOperation(value = "集合存储字段所需要的检验类型，考虑是否直接放在字段表中", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "企业id", paramType = "query", required = true),
            @ApiImplicitParam(name = "ID", value = "表ID", paramType = "query", required = true),
            @ApiImplicitParam(name = "fieldName", value = "字段名", paramType = "query", required = true),
    })
    public ResponseResult<List<String>> checkField(Integer id, Integer ID, String fieldName) {
        List<String> list = new ArrayList<>();
        ResponseResult<List<String>> listResponseResult = new ResponseResult<>(list, CommonCode.SUCCESS);
        return listResponseResult;
    }

    /**
     * 模板导入
     */


    @RequestMapping(value = "/importFile ", method = RequestMethod.GET)
    @ApiOperation(value = "模板导入", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)
    public ResponseResult importFile(String path) {
        List<JSONObject> list = new ArrayList<>();
        ResponseResult<List<JSONObject>> listResponseResult = new ResponseResult<>(list, CommonCode.SUCCESS);
        return listResponseResult;
    }

    /**
     * 模板导出
     */


    @RequestMapping(value = "/exporttFile ", method = RequestMethod.GET)
    @ApiOperation(value = "导出模板", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "List<JsonObject>", value = "json对象集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "path", value = "文档下载路径", paramType = "query", required = true),
    })
    public ResponseResult<Boolean> importFile(List<JSONObject> list, String path) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 文件上传
     * 这里需要传文件的路径，上传的地址由后端简历文件然后确定上传位置
     */
    @RequestMapping(value = "/UploadFile ", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "hkt")
    @ApiImplicitParam(name = "path", value = "文档路径", paramType = "query", required = true)

    public ResponseResult<Boolean> UploadFile(String path) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }


}
