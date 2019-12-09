package com.qinjee.masterdata.dao.staffdao.reportDao;

import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.RegulationDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @program: eTalent
 * @description:
 * @author: penghs
 * @create: 2019-12-09 10:20
 **/
@Repository
public interface ReportFormDao {

    List<RegulationDetailVo> selectOrgIncreaseMemberDetail(@Param("orgId") Integer orgId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<RegulationDetailVo> selectOrgDecreaseMemberDetail(@Param("orgId") Integer orgId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
