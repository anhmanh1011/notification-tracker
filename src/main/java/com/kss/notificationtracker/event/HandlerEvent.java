package com.kss.notificationtracker.event;

import com.kss.notificationtracker.entity.NotificationEntity;
import com.kss.notificationtracker.entity.NotificationItemEntity;
import com.kss.notificationtracker.entity.redis.UserTopicCacheEntity;
import com.kss.notificationtracker.message.NotificationModel;
import com.kss.notificationtracker.repository.NotificationRepository;
import com.kss.notificationtracker.repository.redis.UserTopicCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@Slf4j
public class HandlerEvent {

    @Autowired
    KafkaTemplate<String, NotificationModel> kafkaTemplate;

//    public static final String TOPIC_HANDLER = "push-notification-handler";

    public static final String TOPIC_HANDLER = "push-notification-handler3";

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserTopicCacheRepository userTopicCacheRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @KafkaListener(topics = TOPIC_HANDLER,
            groupId = "notification_tracker", concurrency = "2")
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

    public void processSingle(NotificationModel notificationModel) {

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .id(UUID.randomUUID().toString())
                .itemEntity(NotificationItemEntity.builder()
                        .itemId(notificationModel.getId())
                        .body(notificationModel.getBody())
                        .url(notificationModel.getUrl())
                        .title(notificationModel.getTitle())
                        .type(notificationModel.getMessageType().name())
                        .build())
                .userId(notificationModel.getUser())
                .createTime(notificationModel.getTime())
                .read(false)
                .build();
        notificationRepository.save(notificationEntity);
    }


    public void processMulticast(NotificationModel notificationModel) {

        List<NotificationEntity> notificationEntityList = buildListNotificationCollection(notificationModel, notificationModel.getListUser());
        mongoTemplate.insertAll(notificationEntityList);


    }


    public void processTopic(NotificationModel notificationModel) throws Exception {
        System.out.println(notificationModel);

        Optional<UserTopicCacheEntity> optionalTopicEntity = userTopicCacheRepository.findById(notificationModel.getTopic());
        if (optionalTopicEntity.isPresent()) {
            UserTopicCacheEntity topicEntity = optionalTopicEntity.get();
            List<NotificationEntity> notificationEntityList = buildListNotificationCollection(notificationModel, topicEntity.getUsers());
            mongoTemplate.insertAll(notificationEntityList);

        } else {
            log.error("topic " + notificationModel.getTopic() + " not exists");
            throw new Exception("topic " + notificationModel.getTopic() + " not exists");
        }

    }

    private List<NotificationEntity> buildListNotificationCollection(NotificationModel notificationModel, List<String> listUser) {
        return
                listUser.stream().map(s -> NotificationEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .itemEntity(NotificationItemEntity.builder()
                                .itemId(notificationModel.getId())
                                .body(notificationModel.getBody())
                                .url(notificationModel.getUrl())
                                .title(notificationModel.getTitle())
                                .type(notificationModel.getMessageType().name())
                                .build())
                        .userId(s)
                        .createTime(notificationModel.getTime())
                        .read(false)
                        .build()
                ).collect(Collectors.toList());

    }

}
