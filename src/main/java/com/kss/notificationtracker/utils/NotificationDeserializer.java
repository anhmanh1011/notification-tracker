package com.kss.notificationtracker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.notificationtracker.common.SpringContext;
import com.kss.notificationtracker.message.NotificationModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class NotificationDeserializer implements Deserializer<NotificationModel> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public NotificationModel deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), NotificationModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }


}
