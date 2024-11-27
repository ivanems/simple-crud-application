package ru.ivanems.task.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.ivanems.task.dto.TaskDTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskDTO> kafkaTemplate;

    public void send(TaskDTO taskDTO) {

        Message<TaskDTO> message = MessageBuilder.withPayload(taskDTO).build();

        kafkaTemplate.send(message).whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("Task sent successfully: {}", taskDTO);
            } else {
                log.error("Error sending task: {}", taskDTO, exception);
            }
        });

    }

}
