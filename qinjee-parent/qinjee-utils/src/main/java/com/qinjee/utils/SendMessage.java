package com.qinjee.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.PoolingHTTPClient;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 *
 */
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

        public SmsThread(SmsMultiSender msender, String nationCode,int appid,String appkey,
                         int templateId, String smsSign,String[] phoneNumbers,String[] param) {
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
                SmsMultiSender msender = new SmsMultiSender(appid, appkey);
                SmsMultiSenderResult result = msender.sendWithParam("86", phoneNumbers,
                        templateId, param, smsSign, "", "");

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
    public static void sendMessageMany(int appid, String appkey, int templateId, String smsSign, List<String> phoneNumber, List<String> param) {
        // 创建一个连接池 httpclient, 并设置最大连接量为10

        PoolingHTTPClient httpclient = new PoolingHTTPClient(10);

        // 创建 SmsSingleSender 时传入连接池 http client
        SmsMultiSender smsMultiSender = new SmsMultiSender(appid, appkey, httpclient);
        String[] phoneNumbers = (String[]) phoneNumber.toArray();
        String[] params = (String[]) param.toArray();
        // 创建线程
        SmsThread[] threads = new SmsThread[phoneNumber.size()];
        for (int i = 0; i < phoneNumber.size(); i++) {
            threads[i] = new SmsThread(smsMultiSender, "86", appid, appkey, templateId, smsSign, phoneNumbers, params);
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

}
