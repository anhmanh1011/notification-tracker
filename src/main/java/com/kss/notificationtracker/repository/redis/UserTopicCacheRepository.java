package com.kss.notificationtracker.repository.redis;

import com.kss.notificationtracker.entity.redis.UserTopicCacheEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserTopicCacheRepository extends CrudRepository<UserTopicCacheEntity, String> {
}