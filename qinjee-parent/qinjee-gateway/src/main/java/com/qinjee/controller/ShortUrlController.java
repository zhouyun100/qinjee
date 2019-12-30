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

import com.alibaba.fastjson.JSONArray;
import com.qinjee.zull.redis.RedisClusterService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 周赟
 * @date 2019/12/03
 */
@RestController
public class ShortUrlController {

    private static Logger logger = LogManager.getLogger(ShortUrlController.class);

    @Autowired
    private RedisClusterService redisClusterService;

    /**
     * 短链接
     * @param response
     * @param shortUrlCode
     * @return
     */
    @RequestMapping("/{shortUrlCode}")
    public String shortUrl(HttpServletResponse response, @PathVariable("shortUrlCode") String shortUrlCode) {

        try{
            if(StringUtils.isNotBlank(shortUrlCode) ){
               if(redisClusterService.exists ( shortUrlCode ) ){
                   String s = redisClusterService.get ( shortUrlCode );
                    Map < String, Integer >  map = (  Map < String, Integer>  ) JSONArray.parse ( s );
                    StringBuffer stringBuffer=new StringBuffer (  );
                    stringBuffer.append ( "?" );
                   for (Map.Entry < String, Integer > integerStringEntry : map.entrySet ()) {
                    stringBuffer.append ( integerStringEntry.getKey () );
                    stringBuffer.append ( "=" );
                    stringBuffer.append ( integerStringEntry.getValue () );
                   }
                   response.sendRedirect(""+stringBuffer.toString ());
               }
            }

            logger.info("shortUrl request success shortUrlCode={}",shortUrlCode);

        }catch (Exception e){
            e.printStackTrace();
        }
        return shortUrlCode;
    }


}
