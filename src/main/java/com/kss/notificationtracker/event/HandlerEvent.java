package com.kss.notificationtracker.event;

import com.kss.notificationtracker.entity.NotificationEntity;
import com.kss.notificationtracker.message.NotificationModel;
import com.kss.notificationtracker.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;


@Component
@Slf4j
@Validated
public class HandlerEvent {

    @Autowired
    KafkaTemplate<String, NotificationModel> kafkaTemplate;

//    public static final String TOPIC_HANDLER = "push-notification-handler";

    public static final String TOPIC_HANDLER = "push-notification-handler";

    @Autowired
    NotificationRepository notificationRepository;

    @KafkaListener(topics = TOPIC_HANDLER,
            groupId = "notification_tracker")
    public void handler(NotificationModel notificationModel) {
        System.out.println("received message..." + notificationModel);

//        kafkaTemplate.send(TOPIC_HANDLER, notificationModel.getId(), notificationModel);
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .id(notificationModel.getId())
                .body(notificationModel.getBody())
                .read(false)
                .createTime(LocalDateTime.now())
                .url(notificationModel.getUrl())
                .userId(notificationModel.getUser())
                .title(notificationModel.getTitle())
                .build();
        notificationRepository.save(notificationEntity);

    }


}
