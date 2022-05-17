package com.kss.notificationtracker.runner;

import com.kss.notificationtracker.entity.NotificationObjectEntity;
import com.kss.notificationtracker.entity.UserNotificationEntity;
import com.kss.notificationtracker.repository.NotificationObjectEntityRepository;
import com.kss.notificationtracker.repository.UserNotificationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class example implements CommandLineRunner {

    @Autowired
    NotificationObjectEntityRepository notificationObjectEntityRepository;

    @Autowired
    UserNotificationEntityRepository userNotificationEntityRepository;

    public static final String USER_ID = "USER_ID_";

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(50);//creating a pool of 5 threads
        List<Future> futures = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < 5000; i++) {
            executor.execute(() -> {
                List<NotificationObjectEntity> notificationObjectEntities = new ArrayList<>();
                List<UserNotificationEntity> userNotificationEntities = new ArrayList<>();

                for (int j = 0; j < 5000; j++) {
                    String uuid = UUID.randomUUID().toString();

                    NotificationObjectEntity notificationObjectEntity = NotificationObjectEntity.builder()
                            .id(uuid)
                            .body(uuid)
                            .title(uuid)
                            .url(uuid)
                            .createTime(LocalDateTime.now().plus(1, ChronoUnit.YEARS))
                            .build();
                    notificationObjectEntities.add(notificationObjectEntity);


                    UserNotificationEntity notificationEntity = UserNotificationEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .notificationId(uuid)
                            .userId("USER_" + j)
                            .isRead(0)
                            .createTime(LocalDateTime.now().plus(1, ChronoUnit.YEARS))
                            .build();

                    userNotificationEntities.add(notificationEntity);
                }
                notificationObjectEntityRepository.saveAll(notificationObjectEntities);

                userNotificationEntityRepository.saveAll(userNotificationEntities);
                System.out.println("done");

            });
        }
        System.out.println("done");

    }
}
