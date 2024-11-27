package ru.ivanems.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtpAuth}")
    private boolean smtpAuth;

    @Value("${mail.startTlsEnabled}")
    private boolean startTlsEnabled;

    @Value("${mail.sslEnabled}")
    private boolean sslEnabled;

    @Value("${mail.fromEmail}")
    private String fromEmail;

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
