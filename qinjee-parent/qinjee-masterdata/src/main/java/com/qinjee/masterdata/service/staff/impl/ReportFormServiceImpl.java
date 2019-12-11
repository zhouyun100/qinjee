package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.reportDao.ReportFormDao;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.RegulationCountVo;
import com.qinjee.masterdata.model.vo.staff.RegulationDetailVo;
import com.qinjee.masterdata.service.staff.ReportFormService;
import com.qinjee.model.request.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public List<RegulationDetailVo> selectOrgIncreaseMemberDetail(List<Integer> orgIds, Date startDate, Date endDate) {
        return reportFormDao.selectOrgIncreaseMemberDetail(orgIds, startDate, endDate);
    }

    @Override
    public List<RegulationDetailVo> selectOrgDecreaseMemberDetail(List<Integer> orgIds, Date startDate, Date endDate) {
        return reportFormDao.selectOrgDecreaseMemberDetail(orgIds, startDate, endDate);
    }

    @Override
    public List<RegulationCountVo> selectOrgRegulationCount(List<Integer> orgIds, Date startDate, Date endDate, Integer layer, UserSession userSession) {
        List<RegulationCountVo> regulationList = reportFormDao.selectOrgRegulationCount(orgIds, startDate, endDate);
        //拿到单位单位作为一级节点
        List<RegulationCountVo> topRegulationList = regulationList.stream().filter(regulation -> {
            if (regulation.getOrgParentId()!= null && regulation.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //递归处理机构,使其以树形结构展示
        handler(regulationList, topRegulationList);
        return regulationList;
    }
    private void handler(List<RegulationCountVo> regulationCountList, List<RegulationCountVo> topRegulationsList) {

        for (RegulationCountVo regulation : topRegulationsList) {
            Integer orgId = regulation.getOrgId();
            List<RegulationCountVo> childList = regulationCountList.stream().filter(vo -> {
                Integer orgParentId = vo.getOrgParentId();
                if (orgParentId != null && orgParentId >= 0) {
                    return orgParentId.equals(orgId);
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if (childList != null && childList.size() > 0) {


                regulation.setChildList(childList);
                regulationCountList.removeAll(childList);
                handler(regulationCountList, childList);
            }
        }
    }
}
