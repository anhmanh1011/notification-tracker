package com.kss.notificationtracker.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash(value = "TopicNotifyEntity", timeToLive = 60*60*24) // 24h
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicEntity {
    private static final long serialVersionUID = 1708925807375596799L;
    @Id
    String id;
    List<String> listUser;
}
