package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.PositionGradeDao;
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
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月18日 15:41:00
 */
@Service
public class PositionGradeServiceImpl implements PositionGradeService {
    @Autowired
    private PositionGradeDao positionGradeDao;

    @Override
    public List<PositionGrade> getPositionGradeListByPositionId(Integer positionId) {
        return positionGradeDao.getPositionGradeListByPositionId(positionId);
    }

    @Override
    public ResponseResult<PageResult<PositionGrade>> getPositionLevelList(PageVo pageVo) {
        if(pageVo != null && (pageVo.getCurrentPage() != null && pageVo.getPageSize() != null)){
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
        }
        List<PositionGrade> positionGradeList = positionGradeDao.getPositionLevelList();
        PageResult<PositionGrade> pageResult = new PageResult<>(positionGradeList);
        return new ResponseResult<>(pageResult);
    }

    @Override
    public ResponseResult addPositionGrade(PositionGradeVo positionGradeVo, UserSession userSession) {
        List<PositionGrade> positionLevelList = positionGradeDao.getPositionLevelList();
        PositionGrade lastPositionGrade;
        Integer sortId;
        if(!CollectionUtils.isEmpty(positionLevelList)){
            lastPositionGrade = positionLevelList.get(positionLevelList.size() - 1);
            sortId = lastPositionGrade.getSortId() + 1000;
        }else {
            sortId = 1000;
        }
        PositionGrade positionGrade = new PositionGrade();
        BeanUtils.copyProperties(positionGradeVo, positionGrade);
        positionGrade.setSortId(sortId);
        positionGrade.setIsDelete((short) 0);
        positionGrade.setOperatorId(userSession.getArchiveId());
        positionGradeDao.insertSelective(positionGrade);
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult editPositionGrade(PositionGradeVo positionGradeVo, UserSession userSession) {
        PositionGrade positionGrade = new PositionGrade();
        BeanUtils.copyProperties(positionGradeVo, positionGrade);
        positionGrade.setOperatorId(userSession.getArchiveId());
        positionGradeDao.updateByPrimaryKeySelective(positionGrade);
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult deletePositionGrade(UserSession userSession, List<Integer> positionGradeIds) {
        if(!CollectionUtils.isEmpty(positionGradeIds)){
            for (Integer positionGradeId : positionGradeIds) {
                PositionGrade positionGrade = new PositionGrade();
                positionGrade.setOperatorId(userSession.getArchiveId());
                positionGrade.setIsDelete((short) 1);
                positionGrade.setPositionGradeId(positionGradeId);
                positionGradeDao.updateByPrimaryKeySelective(positionGrade);
            }
        }
        return new ResponseResult();
    }
}
