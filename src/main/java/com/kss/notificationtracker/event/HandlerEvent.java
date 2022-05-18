package com.kss.notificationtracker.event;

import com.kss.notificationtracker.entity.NotificationObjectEntity;
import com.kss.notificationtracker.entity.UserNotificationEntity;
import com.kss.notificationtracker.entity.redis.TopicEntity;
import com.kss.notificationtracker.message.NotificationModel;
import com.kss.notificationtracker.repository.NotificationObjectEntityRepository;
import com.kss.notificationtracker.repository.UserNotificationEntityRepository;
import com.kss.notificationtracker.repository.redis.TopicRepository;
import com.kss.notificationtracker.service.BatchNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


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

    @Autowired
    BatchNotificationService batchNotificationService;

    @Autowired
    TopicRepository topicRepository;

    @KafkaListener(topics = TOPIC_HANDLER,
            groupId = "notification_tracker")
    public void handler(NotificationModel notificationModel) throws Exception {
        System.out.println("received message..." + notificationModel);

        switch (notificationModel.getSendType()) {
            case TOPIC:
                processTopic(notificationModel);
                break;
            case SINGLE:
                processSingle(notificationModel);
                break;
            case MULTICAST:
                processMulticast(notificationModel);
                break;
        }

    }

    @Transactional
    public void processSingle(NotificationModel notificationModel) {
        LocalDateTime now = LocalDateTime.now();

        NotificationObjectEntity notificationObjectEntity = NotificationObjectEntity.builder()
                .id(notificationModel.getId())
                .body(notificationModel.getBody())
                .url(notificationModel.getUrl())
                .title(notificationModel.getTitle())
                .type(notificationModel.getMessageType().name())
                .createTime(now)
                .build();
        notificationObjectEntityRepository.save(notificationObjectEntity);


//        kafkaTemplate.send(TOPIC_HANDLER, notificationModel.getId(), notificationModel);
        UserNotificationEntity notificationEntity = UserNotificationEntity.builder()
                .id(UUID.randomUUID().toString())
                .notificationId(notificationModel.getId())
                .userId(notificationModel.getUser())
                .isRead(false)
                .createTime(LocalDateTime.now())
                .build();
        userNotificationEntityRepository.save(notificationEntity);
    }


    @Transactional
    public void processMulticast(NotificationModel notificationModel) {
        LocalDateTime now = LocalDateTime.now();
        NotificationObjectEntity notificationObjectEntity = NotificationObjectEntity.builder()
                .id(notificationModel.getId())
                .body(notificationModel.getBody())
                .url(notificationModel.getUrl())
                .title(notificationModel.getTitle())
                .type(notificationModel.getMessageType().name())
                .createTime(now)
                .build();

        notificationObjectEntityRepository.save(notificationObjectEntity);
        List<UserNotificationEntity> userNotificationEntities = new ArrayList<>(notificationModel.getListUser().size());
        for (String user :
                notificationModel.getListUser()) {

            UserNotificationEntity notificationEntity = UserNotificationEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .notificationId(notificationModel.getId())
                    .userId(user)
                    .isRead(false)
                    .createTime(now)
                    .build();
            userNotificationEntities.add(notificationEntity);
        }

        batchNotificationService.saveAllJdbcBatch(userNotificationEntities);

    }


    @Transactional
    public void processTopic(NotificationModel notificationModel) throws Exception {
        System.out.println(notificationModel);
        LocalDateTime now = LocalDateTime.now().plus(3, ChronoUnit.YEARS);
        NotificationObjectEntity notificationObjectEntity = NotificationObjectEntity.builder()
                .id(notificationModel.getId())
                .body(notificationModel.getBody())
                .url(notificationModel.getUrl())
                .title(notificationModel.getTitle())
                .type(notificationModel.getMessageType().name())
                .createTime(now)
                .build();

        notificationObjectEntityRepository.save(notificationObjectEntity);

        Optional<TopicEntity> optionalTopicEntity = topicRepository.findById(notificationModel.getTopic());
        if (optionalTopicEntity.isPresent()) {
            TopicEntity topicEntity = optionalTopicEntity.get();
            List<UserNotificationEntity> collect = topicEntity.getListUser().stream().map(s -> {

                UserNotificationEntity notificationEntity = UserNotificationEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .notificationId(notificationModel.getId())
                        .userId(s)
                        .isRead(false)
                        .createTime(now)
                        .build();
                return notificationEntity;
            }).collect(Collectors.toList());

            batchNotificationService.saveAllJdbcBatch(collect);
        } else {
            log.error("topic " + notificationModel.getTopic() + " not exists");
            throw new Exception("topic " + notificationModel.getTopic() + " not exists");
        }

    }

}
