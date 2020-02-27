package com.qinjee.utils;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.PoolingHTTPClient;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 */
public class SendMessage {
    public static class SmsThread extends Thread {
        private final SmsMultiSender msender;
        private final String nationCode;
        private String[] phoneNumbers;
        private String[] param;
        private int appid;
        private String appkey;
        private int templateId;
        private String smsSign;

        public SmsThread(SmsMultiSender msender, String nationCode, int appid, String appkey,
                         int templateId, String smsSign, String[] phoneNumbers, String[] param) {
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
                msender.sendWithParam("86", phoneNumbers, templateId, param, smsSign, "", "");
            } catch (HTTPException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public static void sendMessageMany(int appid, String appkey, int templateId, String smsSign, List<String> phoneNumber, List<String> param) {
            // 创建一个连接池 httpclient, 并设置最大连接量为10
            PoolingHTTPClient httpclient = new PoolingHTTPClient(10);
            // 创建 SmsSingleSender 时传入连接池 http client
            // 创建线程
            String[] phoneNumbers = new String[phoneNumber.size()];
            phoneNumber.toArray(phoneNumbers);
            String[] params = new String[param.size()];
            param.toArray(params);
            SmsMultiSender smsMultiSender = new SmsMultiSender(appid, appkey, httpclient);
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
        public static void  sendMailSingle(int appid, String appkey,int templateId, String smsSign, List<String> phoneNumber, List<String> param) {
            try {
                SmsSingleSender ssender = new SmsSingleSender (appid, appkey );
                String[] params = new String[param.size()];
                param.toArray(params);
                String[] phoneNumbers = new String[phoneNumber.size()];
                phoneNumber.toArray(phoneNumbers);
                SmsSingleSenderResult result = ssender.sendWithParam ( "86", phoneNumbers[0],
                        templateId, params, smsSign, "", "" );
                System.out.println ( result );
            } catch (HTTPException e) {
                // HTTP 响应码错误
                e.printStackTrace ();
            } catch (JSONException e) {
                // JSON 解析错误
                e.printStackTrace ();
            } catch (IOException e) {
                // 网络 IO 错误
                e.printStackTrace ();
            }
        }
    }
