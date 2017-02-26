package com.oracle.service;

import com.oracle.vo.MultiMailMessage;
import org.springframework.mail.MailMessage;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * Created by oracle on 2017/2/26.
 */
public interface MailService {
    public void sendMailMessage(MultiMailMessage multiMailMessage);
}
