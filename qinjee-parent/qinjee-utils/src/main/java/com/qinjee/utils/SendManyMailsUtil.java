package com.qinjee.utils;

import entity.MailConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.Message;
import javax.mail.BodyPart;
import javax.mail.Transport;
import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.validation.constraints.Max;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Java Mail 工具类
 *
 * @author hkt
 * @version 1.0
 *此处以后根据数据库查询的内容来确定发件人
 * 使用恶汉式单例模式应对高并发
 *
 */
public class SendManyMailsUtil {

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SENDERNICK = "sendernick";

    /**
     * 获取邮件属性配置信息
     * @return
     */
    private static Properties getProperties(MailConfig mailConfig){
        Properties props = System.getProperties();
        if(StringUtils.isNoneBlank(mailConfig.getMailSmtpAuth())){
            props.put(MAIL_SMTP_AUTH, mailConfig.getMailSmtpAuth());
        }
        props.put(MAIL_TRANSPORT_PROTOCOL, mailConfig.getMailTransportProtocol());
        props.put(MAIL_SMTP_HOST, mailConfig.getMailSmtpHost());
        props.put(MAIL_SMTP_PORT, mailConfig.getMailSmtpPort());
        props.put(USERNAME, mailConfig.getUsername());
        props.put(PASSWORD, mailConfig.getPassword());
        props.put(SENDERNICK, mailConfig.getSendernick());
        return props;
    }

    /**
     * 获取邮件会话信息
     * @return
     */
    private static Session getSession(Properties props){
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        return session;
    }

    /**
     * 发送邮件
     * @param mailConfig
     * @param toList 收件人
     * @param copyToList 抄送
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public static boolean sendMail(MailConfig mailConfig, List<String> toList, List<String> copyToList, String subject, String content, List<String> fileList) {
        boolean success = true;
        try {

            Properties props = getProperties(mailConfig);
            Session session = getSession(props);

            MimeMessage mimeMsg = new MimeMessage(session);
            Multipart mp = new MimeMultipart();

            // 设置发件人和昵称
            try {
                // 设置昵称
                String nick = javax.mail.internet.MimeUtility.encodeText((String)props.get(SENDERNICK));
                // 设置发件人
                mimeMsg.setFrom(new InternetAddress((String)props.get(USERNAME), nick));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 设置收件人
            String toListStr = getMailList(toList);
            if(StringUtils.isNotBlank(toListStr)){
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }

            // 设置抄送人
            String ccListStr = getMailList(copyToList);
            if(StringUtils.isNotBlank(toListStr)){
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr));
            }

            // 设置主题
            mimeMsg.setSubject(subject);

            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);

            // 设置附件
            if (fileList != null && fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList.get(i));
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();

            // 发送邮件
            Transport transport = session.getTransport(mailConfig.getMailTransportProtocol());
            transport.connect(mailConfig.getMailSmtpHost(), Integer.valueOf(mailConfig.getMailSmtpPort()), mailConfig.getUsername(), mailConfig.getPassword());
            transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
            transport.close();

        }catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }catch (Exception e){
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 收件人列表转字符串
     * @param mailList
     * @return
     */
    private static String getMailList(List<String> mailList) {
        StringBuffer mailStr = new StringBuffer();
        if(!CollectionUtils.isEmpty(mailList)){
            int listSize = mailList.size();
            for(int i = 0; i < listSize; i++){
                mailStr.append(mailList.get(i));
                if(i < (listSize - 1)){
                    mailStr.append(",");
                }
            }
        }
        return mailStr.toString();
    }

}
