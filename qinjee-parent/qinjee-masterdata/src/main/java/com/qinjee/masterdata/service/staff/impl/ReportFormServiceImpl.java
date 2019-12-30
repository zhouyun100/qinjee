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
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

   /* @Override
    public List<RegulationCountVo> selectOrgRegulationCount(List<Integer> orgIds, Date startDate, Date endDate, Integer layer, UserSession userSession) {

        //查询所有机构节点
        List<RegulationCountVo> regulationList = reportFormDao.selectOrgRegulationCount(orgIds, startDate, endDate);
        //List<RegulationCountVo> regulationList = reportFormDao.selectOrgRegulationCount(startDate, endDate);
        //拿到单位单位作为一级节点
        List<RegulationCountVo> topRegulationList = regulationList.stream().filter(regulation -> {
            if (regulation.getOrgParentId() != null && regulation.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //递归处理机构,使其以树形结构展示
        handler(regulationList, topRegulationList);

        // handlerTotal(regulationList);
        //遍历设计总数


        return regulationList;
    }
*/

    @Override
    public List<RegulationCountVo> selectOrgRegulationCount(List<Integer> orgIds, Date startDate, Date endDate, Integer layer, UserSession userSession) {

        //查询所有机构节点
        //List<RegulationCountVo> allRegulationList = reportFormDao.selectOrgRegulationCount(orgIds, startDate, endDate);
        //倒叙查询
        List<RegulationCountVo> allRegulationList = reportFormDao.selectOrgRegulationCount2(startDate, endDate);
        RegulationCountVo tempVo = null;
        for (RegulationCountVo regulationCountVo : allRegulationList) {
            if (Objects.nonNull(tempVo)) {
                //找到父类进行统计
                for (RegulationCountVo parent : allRegulationList) {
                    if (tempVo.getOrgParentId().equals(parent.getOrgId())) {
                        parent.setBeginNum(parent.getBeginNum() + tempVo.getBeginNum());
                        parent.setAttritionNum(parent.getAttritionNum() + tempVo.getAttritionNum());
                        parent.setEndNum(parent.getEndNum() + tempVo.getEndNum());
                        parent.setIncreasedNum(parent.getIncreasedNum() + tempVo.getIncreasedNum());
                        parent.setLeaveNum(parent.getLeaveNum() + tempVo.getLeaveNum());
                        parent.setNewJoinNum(parent.getNewJoinNum() + tempVo.getNewJoinNum());
                        parent.setRetiredNum(parent.getRetiredNum() + tempVo.getRetiredNum());
                        parent.setTransferInNum(parent.getTransferInNum() + tempVo.getTransferInNum());
                        parent.setTransferOutNum(parent.getTransferOutNum() + tempVo.getTransferOutNum());
                    }
                }
            }
            tempVo = regulationCountVo;
        }
        //筛选出勾选的
        List<RegulationCountVo> regulationList = allRegulationList.stream().filter(reg -> {
            if (orgIds.contains(reg.getOrgId())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        //再次筛选出顶层机构
        List<RegulationCountVo> topRegulationList = regulationList.stream().filter(regulation -> {
            if (regulation.getOrgParentId() != null && regulation.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        handler(regulationList, topRegulationList,layer);
        return regulationList;
    }


    private void handler(List<RegulationCountVo> regulationCountList, List<RegulationCountVo> topRegulationsList,Integer layer) {
        //todo
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
                handler(regulationCountList, childList,layer);
            }
        }
    }
}
