package com.dhflour.dhflourdemo1.batch.service.kafka;

import com.dhflour.dhflourdemo1.batch.config.PushBatchConfig;
import com.dhflour.dhflourdemo1.core.service.fcm.FcmService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
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

/**
 * 푸시 알림 Kafka 소비자 서비스 구현 클래스
 * Kafka로부터 메시지를 수신하여 처리하고, 푸시 알림을 전송한다.
 */
@Slf4j
@Service
public class PushKafkaConsumerServiceImpl implements PushKafkaConsumerService {

    // 고정된 스레드 풀을 사용하여 비동기 작업을 처리하는 ExecutorService
    private final ExecutorService executorService = Executors.newFixedThreadPool(1000); // 이것에 따라 동작 처리
    private static long count = 0;
    private Counter successCounter;
    private Counter failureCounter;
    private Counter retryCounter;

    @Autowired
    private Gson gson;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * 서비스 초기화 메서드, 성공, 실패, 재시도 카운터를 등록한다.
     */
    @PostConstruct
    public void init() {
        successCounter = Counter.builder("push.notification.success")
                .description("Number of successful push notifications")
                .register(meterRegistry);

        failureCounter = Counter.builder("push.notification.failure")
                .description("Number of failed push notifications")
                .register(meterRegistry);

        retryCounter = Counter.builder("push.notification.retry")
                .description("Number of retried push notifications")
                .register(meterRegistry);
    }

    /**
     * KafkaListener를 통해 메시지를 수신하고, 비동기적으로 메시지를 처리한다. 10개 사용
     * @param message 수신한 메시지
     * @param ack 메시지 확인을 위한 Acknowledgment 객체
     */
    @KafkaListener(topics = PushBatchConfig.TOPIC, groupId = "app_push_group", concurrency = "10")
    public void listen(@Payload String message, Acknowledgment ack) {
        count = count + 1;
        log.info("# listen Number : {}", count);

        //  비동기 작성 시작
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

    /**
     * 메시지를 처리하는 메서드, 수신한 JSON 메시지를 파싱하여 푸시 알림을 전송한다.
     * @param message 수신한 메시지
     */
    private void processMessage(String message) {
        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> messageData = gson.fromJson(message, type);
            String recipient = messageData.get("recipient");
            String notificationMessage = messageData.get("message");

            sendPushNotification(recipient, notificationMessage);
        } catch (Exception e) {
            log.error("Failed to process message", e);
            throw e;  // 예외를 다시 던져서 상위 메서드에서 재시도할 수 있도록 함
        }
    }


    /**
     * 푸시 알림을 전송하는 메서드
     * @param recipient 수신자
     * @param notificationMessage 알림 메시지
     */
    private void sendPushNotification(String recipient, String notificationMessage) {
        Timer timer = Timer.builder("push.notification.send")
                .description("Time taken to send push notification")
                .register(meterRegistry);

        try {
            timer.record(() -> {
                fcmService.sendNotification(recipient, "Push Notification", notificationMessage);
            });
            successCounter.increment();
        } catch (Exception e) {
            failureCounter.increment();
            log.error("Failed to send push notification", e);
            throw e;
        }
    }

    /**
     * 재시도를 포함하여 메시지를 처리하는 메서드
     * @param message 수신한 메시지
     */
    public void processMessageWithRetry(String message) {
        retryTemplate.execute(
                context -> {
                    retryCounter.increment();
                    processMessage(message);
                    return null;
                },
                context -> {
                    // RecoveryCallback: 모든 재시도 실패 후 실행되는 블록
                    log.error("Failed to process message after maximum retries: {}", message);
                    handleFailedMessage(message, context.getLastThrowable());
                    return null;
                }
        );
    }

    /**
     * 3번 시도에도 실패한다면 기록
     *
     * @param message       데이터
     * @param lastThrowable 에러
     */
    private void handleFailedMessage(String message, Throwable lastThrowable) {
        log.error("Handle failed message: {}, due to {}", message, lastThrowable.getMessage());
    }

    /**
     * 서비스 종료 시 ExecutorService를 종료한다.
     */
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
