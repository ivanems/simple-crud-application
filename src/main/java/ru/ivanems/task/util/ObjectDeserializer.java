package ru.ivanems.task.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ObjectDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try{
            return super.deserialize(topic, headers, data);
        }catch (Exception e){
            log.warn("Произошла ошибка во время десериализации сообщения {}", new String(data, StandardCharsets.UTF_8));
            return null;
        }
    }


    @Override
    public T deserialize(String topic, byte[] data) {
        try{
            return super.deserialize(topic, data);
        }catch (Exception e){
            log.warn("Произошла сошибка во время десериализации сообщения {}", new String(data, StandardCharsets.UTF_8));
            return null;
        }
    }

}
