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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.WeChatToken;
import entity.WeChatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 * @author 周赟
 * @date 2019/12/18
 */
public class WeChatUtils {

    public static final RestTemplate CLIENT = new RestTemplate();

    /**
     * 开放平台APPID和APPSECRET
     */
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

        String resultBody = CLIENT.getForObject(url, String.class);
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
        String resultBody = CLIENT.getForObject(url, String.class);
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

    /**
     * 获取小程序access_token
     * @return
     */
    public static String getSmallProgramAccessToken(){
        String accessToken = null;
        String appid = "wx3e27e76e4c9e783e";
        String secret = "a001e9ed62799f4ca7e960649c1a1c60";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        String resultBody = CLIENT.getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(resultBody);

        if(StringUtils.isBlank(json.getString(ERRORKEY))){
            accessToken = json.getString("access_token");
        }
        return accessToken;
    }

    /**
     * 输出小程序二维码
     *
     * @param accessToken 存储与更新
     *    access_token 的存储至少要保留 512 个字符空间；
     *    access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
     *          建议开发者使用中控服务器统一获取和刷新 access_token，其他业务逻辑服务器所使用的 access_token 均来自于该中控服务器，
     *          不应该各自去刷新，否则容易造成冲突，导致 access_token 覆盖而影响业务；
     *    access_token 的有效期通过返回的 expire_in 来传达，目前是7200秒之内的值，中控服务器需要根据这个有效时间提前去刷新。
     *          在刷新过程中，中控服务器可对外继续输出的老 access_token，此时公众平台后台会保证在5分钟内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
     *    access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，
     *          这样便于业务服务器在API调用获知 access_token 已超时的情况下，可以触发 access_token 的刷新流程。
     * @param width
     * @param os
     */
    public static void outputQRCodeImageByLogoFile(String accessToken, Integer width, OutputStream os){
        try {
            BufferedImage bufferedImage = outputSmallProgramStream(accessToken , width);
            ImageIO.write(bufferedImage, "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static BufferedImage outputSmallProgramStream(String accessToken, Integer width){

        BufferedImage bufferedImage = null;
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

        try{
            Map<String, Object> params = new HashMap<>();
            params.put("scene","qinjee");
            //二维码的宽度，单位 px，最小 280px，最大 1280px，默认430
            params.put("width", width);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            //必须是json模式的 post
            String body = JSON.toJSONString(params);
            StringEntity entity = new StringEntity(body);
            entity.setContentType("image/png");

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            bufferedImage = ImageIO.read(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static void main(String [] args) throws Exception{
        String accessToken = getSmallProgramAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;

        Map<String, Object> scene = new HashMap<>();
        scene.put("scene_id", 123);

        Map<String, Object> actionInfo = new HashMap<>();
        actionInfo.put("scene", scene);

        Map<String, Object> params = new HashMap<>();
        params.put("expire_seconds",300);
        params.put("action_name","QR_SCENE");
        params.put("action_info",actionInfo);

        String body = JSON.toJSONString(params);

        HttpEntity httpEntity = new HttpEntity(body);
        ResponseEntity<String> stringResponseEntity = CLIENT.exchange(url, HttpMethod.POST, httpEntity ,String.class);
        System.out.println(stringResponseEntity.getBody());
    }

}
