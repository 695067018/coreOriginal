package com.sug.core.platform.mail;

import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by A on 14-11-9.
 */
public class MailSender {

    private static Logger logger = LoggerFactory.getLogger(MailSender.class);

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Autowired
    private JavaMailSender mailSender;

    public void send(Mail mail) throws MessagingException {

        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");


            helper.setFrom(from);
            helper.setTo(mail.getTo());
            if(StringUtils.hasText(mail.getCc()))
                helper.setCc(mail.getCc());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getText(), true);
            mailSender.send(mime);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new InvalidRequestException("email","发送邮件失败.");
        }

    }

}
