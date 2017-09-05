package com.sug.core.platform.mail.smtp;

import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;

/**
 * Created by user on 2016/3/30.
 */
public abstract class SMTPMailService {
    @Value("${smpt.host}")
    private String smpt_host;
    @Value("${smpt.port}")
    private Integer smtp_port;

    @Value("${smpt.account}")
    private String smpt_account;
    @Value("${smpt.password}")
    private String smpt_password;

    public String getSmpt_host() {
        return smpt_host;
    }

    public Integer getSmtp_port() {
        return smtp_port;
    }

    public String getSmpt_account() {
        return smpt_account;
    }

    public String getSmpt_password() {
        return smpt_password;
    }

    public abstract void sendMail(String email, String subject, String content);

    public abstract void sendMailWithAttachment(String email, String subject, String content, String fileName) throws UnsupportedEncodingException;

    public abstract void sendMailsWithAttachment(String[] emails, String subject, String content, String fileName) throws UnsupportedEncodingException;
}
