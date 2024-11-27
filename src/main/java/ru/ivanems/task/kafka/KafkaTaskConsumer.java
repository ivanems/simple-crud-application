package ru.ivanems.task.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.ivanems.task.dto.TaskDTO;
import ru.ivanems.task.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${task.kafka.client.topic}", groupId = "${task.kafka.consumer.group-id}")
    public void listen(@Payload TaskDTO taskDTO, Acknowledgment ack) {

        try {
            notificationService.sendNotification(taskDTO);
            log.info("Task processed successfully: {}", taskDTO);
        } catch (Exception e) {
            log.error("Error processing task: {}", taskDTO, e);
        } finally {
            ack.acknowledge();
        }

    }

}
