package ru.ivanems.task.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.ivanems.task.dto.TaskDTO;
import ru.ivanems.task.kafka.KafkaTaskProducer;
import ru.ivanems.task.util.ObjectDeserializer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${task.kafka.consumer.group-id}")
    private String groupId;

    @Value("${task.kafka.server}")
    private String server;

    @Value("${task.kafka.session.timeout.as:15000}")
    private String sessionTimeout;

    @Value("${task.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;

    @Value("${task.kafka.max.poll.records:1}")
    private String maxPollRecords;

    @Value("${task.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervals;

    @Value("${task.kafka.client.topic}")
    private String topic;

    @Bean
    public ConsumerFactory<String, TaskDTO> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class);
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.ivanems.task.dto.TaskDTO");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervals);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ObjectDeserializer.class);
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ObjectDeserializer.class);

        DefaultKafkaConsumerFactory<String, TaskDTO> factory = new DefaultKafkaConsumerFactory<>(properties);
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TaskDTO> kafkaListenerContainerFactory(@Qualifier("consumerFactory") ConsumerFactory<String, TaskDTO> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TaskDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000L);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000L, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            log.error("RetryListener: record = {}, exception = {}, deliveryAttempt = {}", record, ex.getMessage(), deliveryAttempt);
        }));
        return handler;
    }

    @Bean("client")
    public KafkaTemplate<String, TaskDTO> kafkaTemplate(ProducerFactory<String, TaskDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTaskProducer producerClient(@Qualifier("client") KafkaTemplate<String, TaskDTO> kafkaTemplate) {
        kafkaTemplate.setDefaultTopic(topic);
        return new KafkaTaskProducer(kafkaTemplate);
    }

    @Bean
    public ProducerFactory<String, TaskDTO> producerFactory () {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(properties);
    }


}
