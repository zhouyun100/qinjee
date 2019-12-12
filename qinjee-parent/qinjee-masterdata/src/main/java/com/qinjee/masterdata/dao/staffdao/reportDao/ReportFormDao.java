package com.qinjee.masterdata.dao.staffdao.reportDao;

import com.qinjee.masterdata.model.vo.organization.UserArchiveVo;
import com.qinjee.masterdata.model.vo.staff.RegulationCountVo;
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

    List<RegulationDetailVo> selectOrgIncreaseMemberDetail(@Param("orgIdList")List<Integer> orgIdList, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<RegulationDetailVo> selectOrgDecreaseMemberDetail(@Param("orgIdList")List<Integer> orgIdList, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<RegulationCountVo> selectOrgRegulationCount(@Param("orgIdList")List<Integer> orgIdList,@Param("startDate")Date startDate, @Param("endDate")Date endDate);
    List<RegulationCountVo> selectOrgRegulationCount2(@Param("startDate")Date startDate, @Param("endDate")Date endDate);
}
