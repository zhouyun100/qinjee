package com.qinjee.masterdata.aop;
import com.qinjee.model.response.ResponseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class ParamAspect {
    @Around("execution(* com.qinjee..*Controller.*(..)) )")
    public void aroundController(ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();
        boolean b = checkParam(args);
        if(!b){
            System.out.println("此次调用参数不合法");
        }
    }

    /**
     * 检验参数
     * @param params
     * @return
     */
    public boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }
}