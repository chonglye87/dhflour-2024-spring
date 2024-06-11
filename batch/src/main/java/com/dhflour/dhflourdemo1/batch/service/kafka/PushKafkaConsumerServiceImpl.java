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
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class PushKafkaConsumerServiceImpl implements PushKafkaConsumerService {

    private static long count = 0;

    @Autowired
    private Gson gson;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private RetryTemplate retryTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1000); // 이것에 따라 동작 처리

    @KafkaListener(topics = PushBatchConfig.TOPIC, groupId = "app_push_group", concurrency = "10")
    public void listen(@Payload String message, Acknowledgment ack) {
        count = count + 1;
        log.info("# listen Number : {}", count);

        CompletableFuture.runAsync(() -> {
            try {
                processMessageWithRetry(message);
                ack.acknowledge();
            } catch (Exception e) {
                log.error("Failed to process message after retries", e);
                // 실패한 메시지를 별도로 저장하거나 모니터링 시스템에 전송하는 로직 추가 가능
            }
        }, executorService).exceptionally(ex -> {
            log.error("Failed to process message", ex);
            return null;
        });
    }


    private void processMessageWithRetry(String message) {
        retryTemplate.execute(
                context -> {
                    processMessage(message);
                    return null;
                },
                context -> {
                    handleFailedMessage(message, context.getLastThrowable());
                    return null;
                }
        );
    }

    /**
     * 3번 시도에도 실패한다면 기록
     * @param message 데이터
     * @param lastThrowable 에러
     */
    private void handleFailedMessage(String message, Throwable lastThrowable) {
        log.error("Handle failed message: {}, due to {}", message, lastThrowable.getMessage());
    }

    private void processMessage(String message) {
        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> messageData = gson.fromJson(message, type);
            String recipient = messageData.get("recipient");
            String notificationMessage = messageData.get("message");

            fcmService.sendNotification(recipient, "Push Notification", notificationMessage);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw e;
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
