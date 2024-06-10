package com.dhflour.dhflourdemo1.batch.service.kafka;

import com.dhflour.dhflourdemo1.batch.config.PushBatchConfig;
import com.dhflour.dhflourdemo1.core.service.fcm.FcmService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class PushKafkaConsumerServiceImpl implements PushKafkaConsumerService {

    @Autowired
    private Gson gson;

    @Autowired
    private FcmService fcmService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1000); // 이것에 따라 동작 처리

    @KafkaListener(topics = PushBatchConfig.TOPIC, groupId = "app_push_group", concurrency = "10")
    public void listen(@Payload String message, Acknowledgment ack) {
        CompletableFuture.runAsync(() -> processMessage(message), executorService)
                .thenRun(ack::acknowledge)
                .exceptionally(ex -> {
                    log.error("Failed to process message", ex);
                    return null;
                });
    }

    private void processMessage(String message) {
        try {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> messageData = gson.fromJson(message, type);
            String recipient = messageData.get("recipient");
            String notificationMessage = messageData.get("message");

            fcmService.sendNotification(recipient, "Push Notification", notificationMessage);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            // 실패한 메시지를 처리하는 로직을 여기에 추가할 수 있습니다.
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
