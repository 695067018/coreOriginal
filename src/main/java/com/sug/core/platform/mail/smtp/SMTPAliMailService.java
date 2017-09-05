package com.sug.core.platform.mail.smtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by user on 2016/3/30.
 */
@Component
@Scope(value = "prototype")
public class SMTPAliMailService extends SMTPMailService {

    private final Logger logger = LoggerFactory.getLogger(SMTPAliMailService.class);

    private final Properties props;

    public SMTPAliMailService() {
        this.props = new Properties();
    }

    @PostConstruct
    public void initProps() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", getSmpt_host());
        props.put("mail.smtp.port", getSmtp_port());
        props.put("mail.user", getSmpt_account());
        props.put("mail.password", getSmpt_password());
    }

    @Override
    public void sendMail(String email, String subject, String content) {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));

            message.setFrom(form);

            // 设置收件人
            InternetAddress to = new InternetAddress(email);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(subject, "utf-8");
            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("send email by aliMail failed: " + e.getMessage());
        }
    }

    @Override
    public void sendMailWithAttachment(String email, String subject, String content, String fileName) throws UnsupportedEncodingException {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));

            message.setFrom(form);

            // 设置收件人
            InternetAddress to = new InternetAddress(email);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(subject, "utf-8");
            // 设置邮件的内容体
            MimeBodyPart mainContent = new MimeBodyPart();

            mainContent.setText(content,"utf-8");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(mainContent);

            // 设置邮件附件
            BodyPart attachment = new MimeBodyPart();

            DataSource source = new FileDataSource(fileName);

            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(fileName);
            multipart.addBodyPart(attachment);

            message.setContent(multipart);
            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("send email by aliMail failed: " + e.getMessage());
        }
    }

    @Override
    public void sendMailsWithAttachment(String[] emails, String subject, String content, String fileName) throws UnsupportedEncodingException {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));

            message.setFrom(form);

            // 设置收件人
            InternetAddress[] sendTo = new InternetAddress[emails.length];
            for (int i = 0; i < emails.length; i++) {
                sendTo[i] = new InternetAddress(emails[i]);
            }
            message.setRecipients(MimeMessage.RecipientType.TO, sendTo);

            // 设置邮件标题
            message.setSubject(subject, "utf-8");
            // 设置邮件的内容体
            MimeBodyPart mainContent = new MimeBodyPart();

            mainContent.setText(content,"utf-8");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(mainContent);

            // 设置邮件附件
            BodyPart attachment = new MimeBodyPart();

            DataSource source = new FileDataSource(fileName);

            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(fileName);
            multipart.addBodyPart(attachment);

            message.setContent(multipart);
            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("send email by aliMail failed: " + e.getMessage());
        }
    }
}

