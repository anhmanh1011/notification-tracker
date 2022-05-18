package com.kss.notificationtracker.repository;

import com.kss.notificationtracker.entity.NotificationObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationObjectEntityRepository extends JpaRepository<NotificationObjectEntity, String> {
}