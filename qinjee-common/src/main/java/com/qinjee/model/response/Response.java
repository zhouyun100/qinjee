package com.qinjee.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 响应结果类
 */

@Data
@ToString
@NoArgsConstructor
public class Response{

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

    public Response(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static Response SUCCESS(){
        return new Response(CommonCode.SUCCESS);
    }
    public static Response FAIL(){
        return new Response(CommonCode.FAIL);
    }

}
