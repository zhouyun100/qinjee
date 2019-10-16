package com.qinjee.masterdata.aop;
import org.aspectj.lang.ProceedingJoinPoint;
//@Aspect
//@Component
public class ParamAspect {
//    @Around("execution(* com.qinjee..*Controller.*(..)) )")
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