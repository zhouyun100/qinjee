package com.qinjee.masterdata.dao.staffdao.reportDao;

import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;

import java.util.Date;
import java.util.List;

/**
 * @program: eTalent
 * @description:
 * @author: penghs
 * @create: 2019-12-09 10:20
 **/
public interface ReportFormDao {
    List<UserArchiveVo> selectOrgIncreaseMemberDetail(Integer orgId, Date startDate, Date endDate);
}
