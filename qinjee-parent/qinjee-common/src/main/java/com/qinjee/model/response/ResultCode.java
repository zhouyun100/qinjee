package com.qinjee.model.response;

/**
 * API接口返回数据规范
 * @date 2019/10/16
 * @author 高雄
 */
public interface ResultCode {

    /**
     * 操作是否成功,true为成功，false操作失败
     * @return
     */
    boolean success();

    /**
     * 操作代码
     * @return
     */
    int code();

    /**
     * 提示信息
     * @return
     */
    String message();

}
