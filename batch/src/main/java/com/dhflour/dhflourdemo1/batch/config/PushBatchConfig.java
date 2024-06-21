package com.dhflour.dhflourdemo1.batch.config;

import com.dhflour.dhflourdemo1.jpa.domain.push.PushNotificationEntityRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class PushBatchConfig {
    public static final String TOPIC = "push_notifications";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private Gson gson;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Backoff Policy 설정 (재시도 간격 설정)
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); // 2초 간격

        // Retry Policy 설정 (재시도 횟수 설정)
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // 최대 3번 재시도

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    public Job pushNotificationJob(JobRepository jobRepository, Step pushNotificationStep) {
        return new JobBuilder("pushNotificationJob", jobRepository)
                .start(pushNotificationStep)
                .listener(listener())
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Step pushNotificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet pushNotificationTasklet) {
        return new StepBuilder("pushNotificationStep", jobRepository)
                .tasklet(pushNotificationTasklet, transactionManager)
                .build();
    }
    @Bean
    @StepScope
    public Tasklet pushNotificationTasklet(PushNotificationEntityRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        return (StepContribution contribution, ChunkContext chunkContext) -> {
            log.debug("repository.count() : {}", repository.count());
            // repository.findAll().forEach(notification -> { // Original code commented out
            for (int i = 0; i < 100; i++) { // Loop for generating 100,000 virtual data entries
                try {
                    final String recipient = "recipient" + i + "@example.com"; // Example recipient
                    final String notificationMessage = "This is a test message #" + i; // Example message

                    final String message = gson.toJson(Map.of(
                            "recipient", recipient,
                            "message", notificationMessage
                    ));
                    kafkaTemplate.send(TOPIC, message);
                } catch (Exception e) {
                    log.error("Error sending message to Kafka", e);
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("batch_task_executor");
    }
}