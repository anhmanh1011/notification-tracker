package com.kss.notificationtracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_object", schema = "user_management")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationObjectEntity {
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "body")
    private String body;
    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
