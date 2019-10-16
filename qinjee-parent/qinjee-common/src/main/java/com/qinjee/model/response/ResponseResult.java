package com.qinjee.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 响应结果类
 * @author 高雄
 * @date 2019/10/16
 */
@Data
@ToString
public class ResponseResult<T>{

    /**
     * 操作是否成功
     */
    boolean success;

    /**
     * 操作代码
     */
    int code;

    /**
     * 提示信息
     */
    String message;


    T result;

    public ResponseResult(ResultCode resultCode){
        setResuktCode(resultCode);
    }

    private void setResuktCode(ResultCode resultCode) {
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public ResponseResult(T result, ResultCode resultCode){
        this.result = result;
        setResuktCode(resultCode);
    }

    public ResponseResult(T result){
        this.result = result;
        setResuktCode(CommonCode.SUCCESS);
    }

    public ResponseResult(){
        setResuktCode(CommonCode.SUCCESS);
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }
    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }
    public static ResponseResult LOGIN_MULTIPLE_COMPANY(){
        return new ResponseResult(CommonCode.LOGIN_MULTIPLE_COMPANY);
    }

}
