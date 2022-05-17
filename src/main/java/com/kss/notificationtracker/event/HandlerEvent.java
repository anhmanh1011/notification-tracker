package com.kss.notificationtracker.event;

import com.kss.notificationtracker.entity.NotificationObjectEntity;
import com.kss.notificationtracker.entity.UserNotificationEntity;
import com.kss.notificationtracker.message.NotificationModel;
import com.kss.notificationtracker.repository.NotificationObjectEntityRepository;
import com.kss.notificationtracker.repository.UserNotificationEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
@Slf4j
@Validated
public class HandlerEvent {

    @Autowired
    KafkaTemplate<String, NotificationModel> kafkaTemplate;

//    public static final String TOPIC_HANDLER = "push-notification-handler";

    public static final String TOPIC_HANDLER = "push-notification-handler";

    @Autowired
    NotificationObjectEntityRepository notificationObjectEntityRepository;
    @Autowired
    UserNotificationEntityRepository userNotificationEntityRepository;

    @KafkaListener(topics = TOPIC_HANDLER,
            groupId = "notification_tracker")
    @Transactional
    public void handler(NotificationModel notificationModel) {
        System.out.println("received message..." + notificationModel);


        NotificationObjectEntity notificationObjectEntity = NotificationObjectEntity.builder()
                .id(notificationModel.getId())
                .body(notificationModel.getBody())
                .url(notificationModel.getUrl())
                .title(notificationModel.getTitle())
                .createTime(LocalDateTime.now())
                .build();
        notificationObjectEntityRepository.save(notificationObjectEntity);


//        kafkaTemplate.send(TOPIC_HANDLER, notificationModel.getId(), notificationModel);
        UserNotificationEntity notificationEntity = UserNotificationEntity.builder()
                .id(UUID.randomUUID().toString())
                .notificationId(notificationModel.getId())
                .userId(notificationModel.getUser())
                .isRead(0)
                .createTime(LocalDateTime.now())
                .build();
        userNotificationEntityRepository.save(notificationEntity);

    }


}
