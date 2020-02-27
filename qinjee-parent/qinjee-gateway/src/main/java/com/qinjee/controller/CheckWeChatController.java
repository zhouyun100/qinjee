/**
 * 文件名：CheckWeChatController
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 周赟
 * @date 2019/12/26
 */
@RestController
public class CheckWeChatController {

    private static Logger logger = LogManager.getLogger(CheckWeChatController.class);

    /**
     * 验证来自微信服务器的消息
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return
     */
    @RequestMapping(value = "/checkSignature",method = RequestMethod.GET)
    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {

        Map<String,String> map = new HashMap<>();
        map.put("signature",signature);
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        map.put("echostr",echostr);
        logger.info("checkSignature request success param={}",map.toString());
        return echostr;
    }
}
