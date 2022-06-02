package com.kss.notificationtracker.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash(value = "user_topic") // 24h
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTopicCacheEntity {
    private static final long serialVersionUID = 1708925807375596791L;

    @Id
    String topicName;

    List<String> users;

}
