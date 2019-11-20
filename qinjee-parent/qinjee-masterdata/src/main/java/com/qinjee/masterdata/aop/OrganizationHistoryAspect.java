package com.qinjee.masterdata.aop;

import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.masterdata.model.vo.organization.OrganizationVoo;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.response.ResponseResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        if (Objects.nonNull(returnObj) && (returnObj instanceof ResponseResult)) {
            OrganizationVO orgBean = null;
            ResponseResult responseResult = (ResponseResult) returnObj;
            if (responseResult.getResult() instanceof OrganizationVO) {
                orgBean = (OrganizationVO) responseResult.getResult();
            }
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
        System.out.println("维护历史机构表");
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof OrganizationVoo) {
            OrganizationVoo orgVo = (OrganizationVoo) args[0];
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
}
