package com.kss.notificationtracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_notification", schema = "user_management")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationEntity {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "notification_id")
    private String notificationId;
    @Basic
    @Column(name = "is_read")
    private Integer isRead;
    @Basic
    @Column(name = "create_time")
    private LocalDateTime createTime;

}
