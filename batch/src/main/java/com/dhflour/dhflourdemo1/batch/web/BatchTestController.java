package com.dhflour.dhflourdemo1.batch.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("api/v1/batch")
public class BatchTestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job pushNotificationJob;

    @GetMapping("/test1")
    public String sendMessage() {
        try {
            log.debug("test start");
            jobLauncher.run(pushNotificationJob, new JobParametersBuilder()
                    .addString("topic", "TOPIC-" + System.currentTimeMillis())  // 새로운 파라미터 추가
                    .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Message sent to Kafka topic!";
    }
}
