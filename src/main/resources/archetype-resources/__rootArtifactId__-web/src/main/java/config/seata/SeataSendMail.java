#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.seata;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-10-26 13:13
 */
public class SeataSendMail {
    public static void main(String[] args) {
        try {
            send1("","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send1(String topic, String content) throws Exception {
        //跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 设置邮件服务器主机名
        p.setProperty("mail.host", SeataMailConfig.host);//指定邮件服务器，默认端口 25
        // 发送服务器需要身份验证
        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);

        // 开启debug调试，以便在控制台查看
        //session.setDebug(true);也可以这样设置
        //p.setProperty("mail.debug", "true");

        // 创建session
        Session session = Session.getDefaultInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //用户名可以用QQ账号也可以用邮箱的别名
                PasswordAuthentication pa = new PasswordAuthentication(SeataMailConfig.username, SeataMailConfig.password);
                // 后面的字符是授权码，用qq密码不行！！
                return pa;
            }
        });

        session.setDebug(true);//设置打开调试状态

        for (int i = 0; i <1; i++) {//发送几封邮件
            //声明一个Message对象(代表一封邮件),从session中创建
            MimeMessage msg = new MimeMessage(session);
            //邮件信息封装
            //1发件人
            msg.setFrom(new InternetAddress(SeataMailConfig.mailFrom));
            //2收件人
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(SeataMailConfig.mailTo));
            //3邮件内容:主题、内容
            msg.setSubject(topic);
            //msg.setContent("Hello, 今天没下雨!!!", "text/plain;charset=utf-8");//纯文本
            //msg.setContent(content, "text/html;charset=utf-8");//发html格式的文本
            msg.setContent(content, "text/html;charset=utf-8");//发html格式的文本
            //发送动作
            Transport.send(msg);
        }
    }
}
