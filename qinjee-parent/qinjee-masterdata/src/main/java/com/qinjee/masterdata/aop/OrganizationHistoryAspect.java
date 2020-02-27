package com.qinjee.masterdata.aop;

import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.response.ResponseResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 机构新增、更新时将机构维护到机构历史表
 */
@Aspect
@Component
public class OrganizationHistoryAspect {

    @Autowired
    private OrganizationHistoryService orgHisService;
    @Autowired
    private OrganizationService orgService;

    @AfterReturning(returning = "returnObj", pointcut = "@annotation(com.qinjee.masterdata.aop.OrganizationSaveAnno)")
    public void afterAddOrganization(JoinPoint joinPoint, Object returnObj) {
        if (Objects.nonNull(returnObj) && (returnObj instanceof OrganizationVO)) {
            OrganizationVO orgBean = (OrganizationVO) returnObj;
            OrganizationHistory orgHisBean = new OrganizationHistory();
            if (Objects.nonNull(orgBean)) {
                BeanUtils.copyProperties(orgBean, orgHisBean);
            }
            orgHisBean.setUpdateTime(new Date());
            orgHisBean.setCreateTime(orgBean.getCreateTime());
            orgHisService.addOrganizationHistory(orgHisBean);
        }
    }

    /**
     * 如果被代理的业务逻辑出现异常，整个过程回滚，包括切面
     *
     * @param joinPoint
     */
    @Before("@annotation(com.qinjee.masterdata.aop.OrganizationEditAnno)")
    public void beforeEditOrganization(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Organization) {
            Organization orgVo = (Organization) args[0];
            OrganizationVO orgBean = orgService.selectByPrimaryKey(orgVo.getOrgId());
            OrganizationHistory orgHisBean = new OrganizationHistory();
            if (Objects.nonNull(orgBean)) {
                BeanUtils.copyProperties(orgBean, orgHisBean);
            }
            orgHisBean.setUpdateTime(new Date());
            orgHisBean.setCreateTime(orgBean.getCreateTime());
            orgHisService.addOrganizationHistory(orgHisBean);
        }
    }

    /**
     *
     *机构划转前，将待划转机构都进行历史维护
     * @param joinPoint
     */
    @Before("@annotation(com.qinjee.masterdata.aop.OrganizationTransferAnno)")
    public void beforeTransferOrganization(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 ) {
            List<Integer> orgIds = (List<Integer>) args[0];
            for (Integer orgId:orgIds){
                addOrganizationHistory(orgId);
            }
        }
    }/**
     *
     *机构删除前，将机构都进行历史维护
     * @param joinPoint
     */
    @Before("@annotation(com.qinjee.masterdata.aop.OrganizationDeleteAnno)")
    public void beforeDeleteOrganization(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 ) {
            List<Integer> orgIds = (List<Integer>) args[0];
            for (Integer orgId:orgIds){
                addOrganizationHistory(orgId);
            }
        }
    }

    public void addOrganizationHistory(Integer orgId){
        OrganizationVO orgBean = orgService.selectByPrimaryKey(orgId);
        OrganizationHistory orgHisBean = new OrganizationHistory();
        if (Objects.nonNull(orgBean)) {
            BeanUtils.copyProperties(orgBean, orgHisBean);
        }
        orgHisBean.setUpdateTime(new Date());
        orgHisBean.setCreateTime(orgBean.getCreateTime());
        orgHisService.addOrganizationHistory(orgHisBean);
    }
}
