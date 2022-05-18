package com.kss.notificationtracker.repository.redis;


import com.kss.notificationtracker.entity.redis.TopicEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<TopicEntity, String> {
}
