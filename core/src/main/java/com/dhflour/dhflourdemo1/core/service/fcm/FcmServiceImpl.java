package com.dhflour.dhflourdemo1.core.service.fcm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmServiceImpl implements FcmService {

    @Override
    public void sendNotification(String token, String title, String body) {
//        Message message = Message.builder()
//                .putData("title", title)
//                .putData("body", body)
//                .setToken(token)
//                .build();

//        try {
//            String response = FirebaseMessaging.getInstance().send(message);
//            System.out.println("Successfully sent message: " + response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            // 2초 지연
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 복구합니다.
            log.error("Thread was interrupted", e);
        }
        log.info("==========================================");
        log.info("= Sending FCM token: {}", token);
        log.info("= Sending FCM title: {}", title);
        log.info("= Sending FCM body: {}", body);
        log.info("==========================================");
        // Device 만료에 대한 예외 처리
//        throw new RuntimeException("Not implemented");
    }
}
