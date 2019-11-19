package com.qinjee.masterdata.aop;

import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.OrganizationHistory;
import com.qinjee.masterdata.service.organation.OrganizationHistoryService;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.DateFormatUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


/**
 * 机构新增、更新时将机构维护到机构历史表
 * 
 */
@Aspect
@Component
public class OrganizationHistoryAspect {

    @Autowired
    private OrganizationHistoryService orgHisService;
    
    @AfterReturning(returning = "returnObj",pointcut = "@annotation(OrganizationAnoDot)")
    public void  afterAddOrganization(JoinPoint joinPoint,Object returnObj){
        if(Objects.nonNull(returnObj)&&(returnObj instanceof ResponseResult)){
            Organization orgBean=null;
            ResponseResult responseResult=(ResponseResult)returnObj;
            if(responseResult.getResult() instanceof Organization){
                 orgBean= (Organization) responseResult.getResult();
            }
            OrganizationHistory orgHisBean = new OrganizationHistory();
            if(Objects.nonNull(orgBean)){
                BeanUtils.copyProperties(orgBean,orgHisBean);
            }
            System.out.println(orgBean);
            orgHisBean.setUpdateTime(new Date());
            orgHisBean.setCreateTime(orgBean.getCreateTime());
            orgHisService.addOrganizationHistory(orgHisBean);
        }
    }

}
