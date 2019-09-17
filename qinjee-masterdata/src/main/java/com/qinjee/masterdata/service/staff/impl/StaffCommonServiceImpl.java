package com.qinjee.masterdata.service.staff.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.CustomTableDao;
import com.qinjee.masterdata.model.entity.CustomTable;
import com.qinjee.masterdata.service.staff.IStaffCommonService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class StaffCommonServiceImpl implements IStaffCommonService {
    private static final Logger logger = LoggerFactory.getLogger(StaffCommonServiceImpl.class);
    @Autowired
    private CustomTableDao customTableDao;

    @Override
    public ResponseResult insert(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            int insert = 0;
            try {
                insert = customTableDao.insert(customTable);
            } catch (Exception e) {
                logger.error("自定义表插入失败");
            }
            if (insert > 0) {
                return new ResponseResult<>(true, CommonCode.SUCCESS);
            } else {
                return new ResponseResult<>(false, CommonCode.FAIL);
            }
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }

    @Override
    public ResponseResult pretenddeleteByPrimaryKey(List<Integer> list) {
        Integer max = 0;
        try {
            max = customTableDao.selectMaxPrimaryKey();
        } catch (Exception e) {
            logger.error("获取最大主键id失败");
            return new ResponseResult(false, CommonCode.FAIL);
        }
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                return new ResponseResult(false, CommonCode.INVALID_PARAM);
            }
            try {
                customTableDao.PretenddeleteByPrimaryKey(list.get(i));
            } catch (Exception e) {
                logger.error("假删除自定义表失败");
            }
        }
        return new ResponseResult(true, CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult updateByPrimaryKey(CustomTable customTable) {
        if (customTable instanceof CustomTable) {
            try {
                customTableDao.updateByPrimaryKey(customTable);
            } catch (Exception e) {
                logger.error("更新自定义表失败");
                return new ResponseResult(false, CommonCode.FAIL);
            }
            return new ResponseResult(true, CommonCode.SUCCESS);
        } else {
            return new ResponseResult<>(false, CommonCode.INVALID_PARAM);
        }
    }
    @Override
    public ResponseResult<PageResult<CustomTable>> selectCustomTable(Integer currentPage,Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);

        return null;
    }

}
