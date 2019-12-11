package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.RegulationCountVo;
import com.qinjee.masterdata.model.vo.staff.RegulationDetailVo;
import com.qinjee.model.request.UserSession;

import java.util.Date;
import java.util.List;

/**
 * @program: eTalent
 * @description: 报表
 * @author: penghs
 * @create: 2019-12-09 10:12
 **/
public interface ReportFormService {
    List<RegulationDetailVo> selectOrgIncreaseMemberDetail(List<Integer> orgIds, Date startDate, Date endDate);

    List<RegulationDetailVo> selectOrgDecreaseMemberDetail(List<Integer> orgIds, Date startDate, Date endDate);

    List<RegulationCountVo> selectOrgRegulationCount(List<Integer> orgId, Date startDate, Date endDate, Integer layer, UserSession userSession);
}
