package ru.ivanems.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.ivanems.task.config.MailConfig;
import ru.ivanems.task.dto.TaskDTO;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final MailConfig mailConfig;

    private final JavaMailSender javaMailSender;

    public void sendNotification(TaskDTO taskDTO) {
        sendNotification(Collections.singletonList(taskDTO));
    }

    public void sendNotification(List<TaskDTO> taskDTO) {

        for (TaskDTO task : taskDTO) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(mailConfig.getToEmail());
            mailMessage.setSubject("Task notification");
            mailMessage.setText(taskDTO.toString());
            javaMailSender.send(mailMessage);

            log.info("Notification sent successfully: {}", taskDTO);
        }
    }

}
