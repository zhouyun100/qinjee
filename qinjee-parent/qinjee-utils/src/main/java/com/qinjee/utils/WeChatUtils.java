/**
 * 文件名：WeChatUtils
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/12/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.utils;

import com.alibaba.fastjson.JSONObject;
import entity.WeChatToken;
import entity.WeChatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

/**
 * 微信工具类
 * @author 周赟
 * @date 2019/12/18
 */
public class WeChatUtils {

    public static final RestTemplate client = new RestTemplate();
    public static final HttpMethod method = HttpMethod.GET;
    public static final String APPID = "wx0fbbbb3716bc7b87";
    public static final String SECRET = "944e365bc56c68ae4cb69d30e670863a";
    public static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
    public static final String GRANT_TYPE_REFRESH = "refresh_token";
    public static final String ERRORKEY = "errcode";

    /**
     * 获取用户access_token
     * @param code
     * @return
     */
    public static WeChatToken getWeChatToken(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + SECRET + "&code=" + code + "&grant_type=" + GRANT_TYPE_AUTHORIZATION;
        return clientExchangeWeChatToken(url);
    }

    /**
     * 刷新access_token
     * @param refreshToken
     * @return
     */
    public static WeChatToken refreshWeChatToken(String refreshToken){
        if(StringUtils.isBlank(refreshToken)){
            return null;
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + APPID + "&grant_type=" + GRANT_TYPE_REFRESH + "&refresh_token=" + refreshToken;
        return clientExchangeWeChatToken(url);
    }

    /**
     * 获取微信用户信息
     * @param weChatToken
     * @return
     */
    public static WeChatUserInfo getWeChatUserInfo(WeChatToken weChatToken){
        if(weChatToken == null){
            return null;
        }
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + weChatToken.getAccessToken() + "&openid=" + weChatToken.getOpenid();
        return clientExchangeWeChatUserInfo(url);
    }

    /**
     * 调用API获取微信token
     * @param url
     * @return
     */
    private static WeChatToken clientExchangeWeChatToken(String url){
        WeChatToken weChatToken = null;

        ResponseEntity<String> response = client.exchange(url, method,null, String.class);
        String resultBody = response.getBody();

        JSONObject json = JSONObject.parseObject(resultBody);

        if(StringUtils.isBlank(json.getString(ERRORKEY))){
            weChatToken = new WeChatToken();
            weChatToken.setAccessToken(json.getString("access_token"));
            String expiresIn = json.getString("expires_in");
            if(StringUtils.isNoneBlank(expiresIn)){
                weChatToken.setExpiresIn(Integer.valueOf(expiresIn));
            }
            weChatToken.setRefreshToken(json.getString("refresh_token"));
            weChatToken.setOpenid(json.getString("openid"));
            weChatToken.setScope(json.getString("scope"));
            weChatToken.setUnionid(json.getString("unionid"));
        }

        return weChatToken;
    }

    /**
     * 调用API获取微信用户信息
     * @param url
     * @return
     */
    private static WeChatUserInfo clientExchangeWeChatUserInfo(String url){
        WeChatUserInfo weChatUserInfo = null;
        ResponseEntity<String> response = client.exchange(url, method,null, String.class);
        String resultBody = response.getBody();

        JSONObject json = JSONObject.parseObject(resultBody);

        if(StringUtils.isBlank(json.getString(ERRORKEY))){
            weChatUserInfo = new WeChatUserInfo();
            weChatUserInfo.setCountry(json.getString("country"));
            weChatUserInfo.setUnionid(json.getString("unionid"));
            weChatUserInfo.setProvince(json.getString("province"));
            weChatUserInfo.setCity(json.getString("city"));
            weChatUserInfo.setOpenId(json.getString("openid"));
            weChatUserInfo.setSex(json.getString("sex"));
            String nickName = json.getString("nickname");
            try{
                nickName = UnicodeUtils.emojiEncode(false, nickName);
                nickName = new String(nickName.getBytes("ISO-8859-1"), "UTF-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            weChatUserInfo.setNickname(nickName);
            weChatUserInfo.setHeadimgurl(json.getString("headimgurl"));
            weChatUserInfo.setLanguage(json.getString("language"));
            weChatUserInfo.setPrivilege(json.getString("privilege"));
        }
        return weChatUserInfo;
    }

    public static void main(String [] args){
        //生成token
        WeChatToken weChatToken = getWeChatToken("071qObFL0rb7I72JgaDL0Y3cFL0qObFz");

        //刷新token
//        weChatToken = refreshWeChatToken(weChatToken.getRefreshToken());

        //获取微信用户个人信息
//        WeChatToken weChatToken = new WeChatToken();
//        weChatToken.setAccessToken("28_e5KyK7OnScl4yZ7fkl6M0ItlvKpN2H1L-I1UNXMYE5dUZwxUmNl95QYkRv66K9zt6QY_FR-O_jS1U-X98czg6BooFm03IsFZMeNBS6yWeUU");
//        weChatToken.setOpenid("o2AHzs4lBOaRNBYmJ9VEqajbihxg");
//        WeChatUserInfo weChatUserInfo = getWeChatUserInfo(weChatToken);
//        if(weChatUserInfo != null){
//            System.out.println("nickName：" + weChatUserInfo.getNickname());
//        }else{
//            System.out.println("request fail!");
//        }
    }

}
