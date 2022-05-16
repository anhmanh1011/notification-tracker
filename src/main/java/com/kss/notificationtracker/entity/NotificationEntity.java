package com.kss.notificationtracker.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "notification")
@Data
@Builder
public class NotificationEntity {
    @Id
    private String id;
    @Field("user_id")
    private String userId;
    private String title;
    private String body;
    private String url;
    private boolean read;
    @Field("create_time")
    private LocalDateTime createTime;
}
