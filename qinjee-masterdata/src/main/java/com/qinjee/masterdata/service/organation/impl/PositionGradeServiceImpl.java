package com.qinjee.masterdata.service.organation.impl;

import com.qinjee.masterdata.dao.PositionGradeDao;
import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.service.organation.PositionGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
