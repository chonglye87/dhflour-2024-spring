package com.dhflour.dhflourdemo1.batch.service.kafka;

import com.dhflour.dhflourdemo1.batch.config.PushBatchConfig;
import com.dhflour.dhflourdemo1.core.service.fcm.FcmService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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

    private final ExecutorService executorService = Executors.newFixedThreadPool(100);

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
            Map messageData = gson.fromJson(message, Map.class);
            String recipient = messageData.get("recipient").toString();
            String notificationMessage = messageData.get("message").toString();

            fcmService.sendNotification(recipient, "Push Notification", notificationMessage);
        } catch (Exception e) {
            log.error("Failed to process message", e);
        }
    }
}
