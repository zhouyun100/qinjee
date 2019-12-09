package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;

import java.util.Date;
import java.util.List;

/**
 * @program: eTalent
 * @description: 报表
 * @author: penghs
 * @create: 2019-12-09 10:12
 **/
public interface ReportFormService {
    List<UserArchiveVo> selectOrgIncreaseMemberDetail(Integer orgId, Date startDate, Date endDate);
}
