/**
 * 文件名：ShortUrlController
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/3
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周赟
 * @date 2019/12/03
 */
@RestController
public class ShortUrlController {


    private static Logger logger = LogManager.getLogger(ShortUrlController.class);

    /**
     * 短链接
     * @param response
     * @param shortUrlCode
     * @return
     */
    @RequestMapping("/{shortUrlCode}")
    public String shortUrl(HttpServletResponse response, @PathVariable("shortUrlCode") String shortUrlCode) {

        try{
            if(StringUtils.isNotBlank(shortUrlCode) && shortUrlCode.equals("qinjee")){
                response.sendRedirect("http://www.qinjee.cn?param=" + shortUrlCode);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return shortUrlCode;
    }

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
        System.out.println("checkSignature request success param：" + map.toString());
        return map.toString();
    }
}
