package org.k2.processmining.utils;

import sun.misc.BASE64Encoder;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Aria on 2017/6/22.
 */
public class SendEmail {
    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;  //163邮箱的端口为25
    public static final String FROM = "15626445092@163.com";
    public static final String PWD = "assdd215";

    private static BASE64Encoder base64Encoder = new BASE64Encoder();

    private static Session getSession(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host",HOST);
//        properties.put("mail.store.protocol",PROTOCOL);
        properties.put("mail.smtp.port",PORT);
        properties.put("mail.smtp.auth",true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("FROM:"+FROM);
                System.out.println("PWD:"+PWD);
                return new PasswordAuthentication(FROM,PWD);
            }
        };
        Session session = Session.getDefaultInstance(properties,authenticator);
        return session;
    }

    public static void send(String toEmail,String content){
        Session session = getSession();
        try {
            System.out.println("---send----"+content);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] addresses = {new InternetAddress(toEmail)};
            message.setRecipients(Message.RecipientType.TO,addresses);
            message.setSubject("账号激活邮件————流程挖掘平台");
            message.setSentDate(new Date());
            message.setContent(content,"text/html;charset=utf-8");
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String getValCode(String email){
        if ("".equals(email))
            return null;
        MessageDigest md5 = null;
        String encodeStr = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            encodeStr = base64Encoder.encode(md5.digest(email.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
}
