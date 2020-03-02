/**
 * 文件名：SysDictController
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/22
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.controller.sys;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.SysDict;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/22
 */
@Api(tags = "【系统参数】数据字典接口")
@RestController
@RequestMapping("/sysDict")
public class SysDictController extends BaseController {

    private static Logger logger = LogManager.getLogger(SysDictController.class);

    private ResponseResult responseResult;

    @Autowired
    private SysDictService sysDictService;

    @ApiOperation(value="根据字典类型查询字典列表", notes="根据字典类型查询字典列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchSysDictListByDictType",method = RequestMethod.POST)
    public ResponseResult searchSysDictListByDictType(String dictType) {
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<SysDict> sysDictList = sysDictService.searchSysDictListByDictType(dictType);

            if(!CollectionUtils.isEmpty(sysDictList)){
                logger.info("searchSysDictListByDictType！dictType={},archiveId={} ", dictType, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(sysDictList);
            }else{
                logger.info("searchSysDictListByDictType！dictType={},archiveId={} ", dictType, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("查询数据集为空！");
            }

        }catch (Exception e){
            logger.info("searchSysDictListByDictType exception! dictType={},archiveId={},exception={}", dictType, userSession.getArchiveId(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据字典类型查询字典列表操作异常！");
        }
        return responseResult;
    }
}
