package ru.ivanems.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.ivanems.task.dto.TaskDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${mail.toEmail}")
    private String toEmail;

    private final JavaMailSender javaMailSender;

    public void sendNotification(TaskDTO taskDTO) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Task notification");
        mailMessage.setText(taskDTO.toString());
        javaMailSender.send(mailMessage);

        log.info("Notification sent successfully: {}", taskDTO);
    }

}
