package com.qinjee.utils;

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
    // 服务邮箱(from邮箱)
    public static String password = "Kai5211314"; // 邮箱密码
    public static String username = "17629926998@163.com";
    // 发件人昵称
    public static String senderNick = "qinjee科技";

    // 系统属性
    private Properties props;
    // 邮件会话对象
    private Session session;
    // MIME邮件对象
    private Multipart mp;
    // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
    private MimeMessage mimeMsg;

    private static SendManyMailsUtil instance = new SendManyMailsUtil();

    private SendManyMailsUtil() {
        //对发送放进行数据库查询进行匹配
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "25");
        props.put("username", username);
        props.put("password", password);
        // 建立会话
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
    }

    public static SendManyMailsUtil getInstance() {
       return instance;
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人
     * @param copyto 抄送
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, List<String> to, List<String> copyto, String subject, String content, List<String> fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            String[] tos=new String[to.size()];
            to.toArray(tos);
            // 设置收件人
            if (to != null && to.size() > 0) {
                String toListStr = getMailList(tos);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // 设置抄送人
            String[] copytos=new String[to.size()];
            to.toArray(copytos);
            if (copyto != null && copyto.size() > 0) {
                String ccListStr = getMailList(copytos);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            //TODO 需要支持多种格式，如页面，表格，牵扯到数据库的查询等等
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
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password"));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }
    public static String getContent(String type, String payload){
        String content=null;
        if(type.equals("html")){
            //todo 执行拼接成html页面
        }else if(type.equals("table")){
            //todo 拼接成table发送
        }
        return content;
    }
//    public static void main(String[] args) {
//        String from = username;
//        String[] to = {"huangkt@qinjee.cn"};
//        String[] copyto = null;
//        String subject = "测试一下";
//        String content = "这是邮件内容，仅仅是测试，不需要回复.";
//        String[] fileList = {"C:\\Users\\Administrator\\IdeaProjects\\eTalent\\qinjee-utils\\src\\main\\resources\\timg.jpg"};
//        //发送邮件需要获取实例再发送
//        SendManyMailsUtil.getInstance().sendMail(from, to, copyto, subject, content, fileList);
//    }

}
