package com.kss.notificationtracker.service;

import com.kss.notificationtracker.entity.UserNotificationEntity;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Service
@Slf4j
public class BatchNotificationService {
    @Autowired
    HikariDataSource hikariDataSource;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;


    public void saveAllJdbcBatch(List<UserNotificationEntity> userNotificationEntities) {
        System.out.println("insert using jdbc batch");
        String sql = String.format(
                "INSERT INTO %s (id, user_id, notification_id, is_read, create_time) " +
                        "VALUES (?, ?, ?, ?, ?)",
                UserNotificationEntity.class.getAnnotation(Table.class).name()
        );
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            int counter = 0;
            for (UserNotificationEntity notificationEntity : userNotificationEntities) {
                statement.clearParameters();
                statement.setString(1, notificationEntity.getId());
                statement.setString(2, notificationEntity.getUserId());
                statement.setString(3, notificationEntity.getNotificationId());
                statement.setBoolean(4, notificationEntity.getIsRead());
                statement.setTimestamp(5, java.sql.Timestamp.valueOf(notificationEntity.getCreateTime()));
                statement.addBatch();
                if ((counter + 1) % batchSize == 0 || (counter + 1) == userNotificationEntities.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
        } catch (Exception e) {

            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}
