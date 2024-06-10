package com.dhflour.dhflourdemo1.batch.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("### Job started...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("### Job finished...");
    }
}