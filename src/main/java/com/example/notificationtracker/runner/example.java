package com.example.notificationtracker.runner;

import com.example.notificationtracker.entity.NotificationEntity;
import com.example.notificationtracker.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class example implements CommandLineRunner {

    @Autowired
    NotificationRepository notificationRepository;

    public static final String USER_ID = "USER_ID_";

    @Override
    public void run(String... args) throws Exception {


        ExecutorService executor = Executors.newFixedThreadPool(500);//creating a pool of 5 threads
        List<Future> futures = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < 500; i++) {

            Runnable runnable = () -> {
                List<NotificationEntity> notificationEntityByUserIdOrderByCreateTimeDesc = null;

                for (int j = 0; j < 2; j++) {
                    int result = new Random().nextInt(20000 - 1) + 1;
                    String userId = USER_ID + result;

                    notificationEntityByUserIdOrderByCreateTimeDesc =  notificationRepository.getNotificationEntityByUserIdOrderByCreateTimeDesc(userId, Pageable.ofSize(20));
                }
            };
            Future<?> submit = executor.submit(runnable);
            futures.add(submit);
        }
        int count = 500;
        while (true){
            int countDone = 0;
            for (int i = 0; i < count; i++) {
                if(futures.get(i).isDone()){
                    countDone++;
                }
            }
            System.out.println(countDone);
            if(countDone == count){
                System.out.println("done");
                LocalDateTime end = LocalDateTime.now();
                System.out.println(start);
                System.out.println(end);
                System.out.println( Duration.between(start, end).get(ChronoUnit.SECONDS));
                return;
            }
        }
    }
}
