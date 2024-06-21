package com.dhflour.dhflourdemo1.batch.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    private long startTime;
    private long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("Job Started at: {}", startTime);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        log.info("Job Ended at: {}", endTime);
        long totalTime = endTime - startTime;
        log.info("Total Time Taken: {} ms", totalTime);
        startTime = 0;
        endTime = 0;
    }
}