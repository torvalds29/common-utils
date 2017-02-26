package com.oracle.service.impl;

import com.oracle.service.MailService;
import com.oracle.utils.Copy;
import com.oracle.vo.MultiMailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by oracle on 2017/2/26.
 */
@Service
public class MailServiceImpl implements MailService {
   private static Logger  logger = Logger.getLogger(MailServiceImpl.class);
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    MultiMailMessage mailMessage;

    @Override
    public void sendMailMessage(MultiMailMessage multiMailMessage) {
        Copy.create().from(multiMailMessage).to(mailMessage);
        if (mailMessage.isHtmlText()) {
            sendMimeMessage(mailMessage);
        } else {
            sendSimpleMessage(mailMessage);
        }
    }

    /**
     * 发送简单邮件
     *
     * @param simpleMailMessage
     */
    public void sendSimpleMessage(SimpleMailMessage simpleMailMessage) {
        mailSender.send(mailMessage);
        logger.info("success send simpleMail");
    }


    /**
     * 发送html邮件
     *
     * @param multiMailMessage
     */
    public void sendMimeMessage(MultiMailMessage multiMailMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            messageHelper.setText(multiMailMessage.getText(), multiMailMessage.isHtmlText());
            messageHelper.setSubject(multiMailMessage.getSubject());
            if (multiMailMessage.getCc() != null) {
                messageHelper.setCc(multiMailMessage.getCc());
            }
            if (multiMailMessage.getBcc() != null) {
                messageHelper.setBcc(multiMailMessage.getBcc());
            }
            messageHelper.setTo(multiMailMessage.getTo());
            messageHelper.setFrom(multiMailMessage.getFrom());
            if (multiMailMessage.getReplyTo() != null) {
                messageHelper.setReplyTo(multiMailMessage.getReplyTo());
            }
            if (multiMailMessage.getSentDate() != null) {
                messageHelper.setSentDate(multiMailMessage.getSentDate());
            }
            mailSender.send(mimeMessage);
            logger.info("success send htmlMail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
