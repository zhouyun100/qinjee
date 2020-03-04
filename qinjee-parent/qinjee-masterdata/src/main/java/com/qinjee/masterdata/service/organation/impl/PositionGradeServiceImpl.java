package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.organation.PositionGradeDao;
import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.model.vo.organization.PositionGradeVo;
import com.qinjee.masterdata.service.organation.PositionGradeService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:41:00
 */
@Service
public class PositionGradeServiceImpl implements PositionGradeService {
    @Autowired
    private PositionGradeDao positionGradeDao;


    @Override
    public ResponseResult<PageResult<PositionGrade>> getPositionLevelList(PageVo pageVo, UserSession userSession) {
        return null;
    }

    @Override
    public ResponseResult addPositionGrade(PositionGrade positionGrade, UserSession userSession) {
        return null;
    }

    @Override
    public ResponseResult editPositionGrade(PositionGrade positionGrade, UserSession userSession) {
        return null;
    }

    @Override
    public ResponseResult deletePositionGrade(UserSession userSession, List<Integer> positionGradeIds) {
        return null;
    }

    @Override
    public ResponseResult sortPositionGrade(UserSession userSession, List<Integer> positionGradeIds) {
        return null;
    }
}
