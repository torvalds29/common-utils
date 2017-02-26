package com.oracle.config;

import com.oracle.vo.MultiMailMessage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;


/**
 * Created by oracle on 2017/2/26.
 */
@Configuration
public class MailConfig {
    @Bean
    @ConfigurationProperties(prefix = "mail.value")
    public MultiMailMessage mailMessage() {
        return new MultiMailMessage();
    }
}
