package com.qinjee.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import com.github.qcloudsms.httpclient.PoolingHTTPClient;
import org.json.JSONException;

import java.io.IOException;
/**
 * @author Administrator
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

        public static void sendMessage() throws InterruptedException {
            // 创建一个连接池 httpclient, 并设置最大连接量为10
            PoolingHTTPClient httpclient = new PoolingHTTPClient(10);

            // 创建 SmsSingleSender 时传入连接池 http client
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey, httpclient);

            // 创建线程
            SmsThread[] threads = new SmsThread[phoneNumbers.length];
            for (int i = 0; i < phoneNumbers.length; i++) {
                threads[i] = new SmsThread(ssender, "86", phoneNumbers[i], "您验证码是：5678");
            }

            // 运行线程
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }

            // join 线程
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }

            // 关闭连接池 httpclient
            httpclient.close();
        }
}