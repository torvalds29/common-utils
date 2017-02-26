package com.oracle.controller;

import com.oracle.service.MailService;
import com.oracle.vo.MultiMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by oracle on 2017/2/26.
 */
@Controller
public class SenEmailController {
    @Autowired
    MailService mailService;
    @RequestMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(){
        MultiMailMessage mailMessage=new MultiMailMessage();
    /*    mailMessage.setText("测试copy");
        mailMessage.setSubject("测试copy subject");*/
        mailMessage.setText("测试copy<a href=\"http://www.baidu.com\">www.baidu.com </a>");
        mailMessage.setSubject("测试copy subject");
        mailMessage.setHtmlText(true);
        mailService.sendMailMessage(mailMessage);
    }
}
