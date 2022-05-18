package com.kss.notificationtracker.repository;

import com.kss.notificationtracker.entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationEntityRepository extends JpaRepository<UserNotificationEntity, String> {
}