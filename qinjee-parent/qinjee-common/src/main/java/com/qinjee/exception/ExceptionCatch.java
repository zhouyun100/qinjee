package com.qinjee.exception;


import com.google.common.collect.ImmutableMap;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.model.response.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常捕获类
 * ControllerAdvice:控制器增强
 *
 * @author 周赟
 * @date 2019-12-17
 **/
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionCatch.class);

    /**
     * 定义map，配置异常类型所对应的错误代码
     */
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;

    /**
     * 定义map的builder对象，去构建ImmutableMap
     */
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    /**
     * 捕获BusinessException此类异常
     * @param be
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseResult customException(BusinessException be, Throwable ex){

        //记录日志
        LOGGER.error("catch customException！code={};success={};message={}" , be.getResultCode().code(),be.getResultCode().success(),be.getResultCode().message());
        LOGGER.error("catch customException ex:", ex);
        ResultCode resultCode = be.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 捕获Exception此类异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e, Throwable ex){

        //记录日志
        LOGGER.error("catch exception ex:", ex);
        if(EXCEPTIONS == null){
            //EXCEPTIONS构建成功
            EXCEPTIONS = builder.build();
        }

        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if(resultCode !=null){
            return new ResponseResult(resultCode);
        }else{

            //返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {

        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}
