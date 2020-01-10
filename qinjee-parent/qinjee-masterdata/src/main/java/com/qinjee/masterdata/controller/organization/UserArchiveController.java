package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.organization.page.UserArchivePageVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.service.organation.UserArchiveService;
import com.qinjee.masterdata.service.staff.IStaffArchiveService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月12日 10:44:00
 */
@Api(tags = "【机构管理】员工档案信息接口")
@RequestMapping("/userArchive")
@RestController
public class UserArchiveController extends BaseController {
    private static Logger logger = LogManager.getLogger(UserArchiveController.class);

    @Autowired
    private UserArchiveService userArchiveService;

    @Autowired
    private IStaffArchiveService staffArchiveService;

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    @PostMapping("/getUserArchiveList")
    @ApiOperation(value = "根据条件分页查询员工信息")
    public ResponseResult<PageResult<UserArchiveVo>> getUserArchiveList(@RequestBody UserArchivePageVo pageQueryVo) {
        if (checkParam(pageQueryVo)) {
            return userArchiveService.getUserArchiveList(pageQueryVo, getUserSession());
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/addUserArchive")
    @ApiOperation(value = "新增员工档案信息")
    public ResponseResult<Integer> addUserArchive(@RequestBody UserArchiveVo userArchiveVo) {
        if (checkParam(userArchiveVo)) {
            return userArchiveService.addUserArchive(userArchiveVo, getUserSession());
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/uploadAndCheck")
    @ApiOperation(value = "ok,导入用户信息excel并校验", notes = "ok")
    public ResponseResult uploadAndCheck(MultipartFile multfile, HttpServletResponse response) throws Exception {
        //参数判空校验
        if (checkParam(multfile)) {
            long start = System.currentTimeMillis();
            ResponseResult responseResult = userArchiveService.uploadAndCheck(multfile, getUserSession(), response);
            logger.info("导入机构excel并校验耗时："+(System.currentTimeMillis()-start)+"ms");
            return  responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/editUserArchive")
    @ApiOperation(value = "编辑保存员工档案信息")
    public ResponseResult<Integer> editUserArchive(@RequestBody UserArchiveVo userArchiveVo) {
        if (checkParam(userArchiveVo)) {
         userArchiveService.editUserArchive(userArchiveVo, getUserSession());
         return new ResponseResult<>(CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/deleteUserArchive")
    @ApiOperation(value = "删除用户信息,参数Map<Integer,Integer>，map中key为userId，value为archiveId")
    public ResponseResult deleteUserArchive(@RequestBody Map<Integer,Integer> idsMap) throws Exception {
        if (checkParam(idsMap)) {
            userArchiveService.deleteUserArchive(idsMap,getUserSession().getCompanyId());
            return new ResponseResult();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @GetMapping("/importToDatabase")
    @ApiOperation(value = "ok,导入用户信息入库")
    public ResponseResult importToDatabase(@RequestParam("orgExcelRedisKey") String orgExcelRedisKey) {
        if (checkParam(orgExcelRedisKey)) {
            long start = System.currentTimeMillis();
            userArchiveService.importToDatabase(orgExcelRedisKey, getUserSession());
            logger.info("导入机构入库："+(System.currentTimeMillis()-start)+"ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

}
