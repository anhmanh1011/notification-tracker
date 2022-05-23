package com.kss.notificationtracker.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorHandlerMessage implements ErrorHandler {

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {
        e.printStackTrace();
//        System.out.println("error: " + consumerRecord.key() + " value: " + consumerRecord.value());
        System.out.println(e.getMessage());
    }

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?, ?> consumer) {
        e.printStackTrace();
        System.out.println("error: " + consumerRecord.key() + " value: " + consumerRecord.value());
        System.out.println(e.getMessage());
        consumer.commitAsync();
    }

}
