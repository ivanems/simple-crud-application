package ru.ivanems.task.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "task.mail")
public class MailConfig {

    private String host;

    private int port;

    private String username;

    private String password;

    private boolean smtpAuth;

    private boolean startTlsEnabled;

    private boolean sslEnabled;

    private String fromEmail;

    private String toEmail;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTlsEnabled);
        props.put("mail.smtp.ssl.enable", sslEnabled);
        props.put("mail.smtp.from", fromEmail);

        return mailSender;
    }


}
