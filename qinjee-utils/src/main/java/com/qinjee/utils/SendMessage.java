package com.qinjee.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.PoolingHTTPClient;
import org.json.JSONException;

import java.io.IOException;

public class SendMessage {

    public static class SmsThread extends Thread {
        private final SmsMultiSender msender;
        private final String nationCode;
        private  String[] phoneNumbers;
        private String[] param;
        private int appid;
        private String appkey;
        private int templateId;
        private String smsSign;

        public SmsThread(SmsMultiSender msender, String nationCode,int appid,String appkey,int templateId, String smsSign,String[] phoneNumbers,String[] param) {
            this.msender = msender;
            this.nationCode = nationCode;
            this.phoneNumbers = phoneNumbers;
            this.param = param;
            this.appid = appid;
            this.appkey = appkey;
            this.templateId = templateId;
            this.smsSign = smsSign;
        }

        @Override
        public void run() {
            try {
                String[] params = {"5678","3"};
                SmsMultiSender msender = new SmsMultiSender(appid, appkey);
                SmsMultiSenderResult result = msender.sendWithParam("86", phoneNumbers,
                        templateId, params, smsSign, "", "");

                System.out.println(result);
            } catch (HTTPException e) {
                // HTTP 响应码错误
                e.printStackTrace();
            } catch (JSONException e) {
                // JSON 解析错误
                e.printStackTrace();
            } catch (IOException e) {
                // 网络 IO 错误
                e.printStackTrace();
            }
        }
    }
    public static void sendMessageMany(int appid,String appkey,int templateId, String smsSign,String[] phoneNumbers,String[] param) {
        // 创建一个连接池 httpclient, 并设置最大连接量为10

        PoolingHTTPClient httpclient = new PoolingHTTPClient(10);

        // 创建 SmsSingleSender 时传入连接池 http client
        SmsMultiSender smsMultiSender = new SmsMultiSender(appid, appkey, httpclient);


        // 创建线程
        SmsThread[] threads = new SmsThread[phoneNumbers.length];
        for (int i = 0; i < phoneNumbers.length; i++) {
            threads[i] = new SmsThread(smsMultiSender, "86", appid, appkey, templateId, smsSign, phoneNumbers, param);
        }

        // 运行线程
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        // join 线程
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 关闭连接池 httpclient
        httpclient.close();
    }

    public static void main(String[] args) {
        String[] phoneNumbers={"18612406236"};
        String param[]={"5678","3"};
//        sendMessageMany(phoneNumbers,1400249114,"91c94cbe664487bbfb072e717957e08f",param,409797,"勤杰软件");
    }

}
