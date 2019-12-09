package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.staffdao.reportDao.ReportFormDao;
import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.service.staff.ReportFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: eTalent
 * @description:
 * @author: penghs
 * @create: 2019-12-09 10:18
 **/
@Service
public class ReportFormServiceImpl implements ReportFormService {
    @Autowired
    ReportFormDao reportFormDao;

    @Override
    public List<UserArchiveVo> selectOrgIncreaseMemberDetail(Integer orgId, Date startDate, Date endDate) {
       return reportFormDao.selectOrgIncreaseMemberDetail(orgId,startDate,endDate);
    }
}
