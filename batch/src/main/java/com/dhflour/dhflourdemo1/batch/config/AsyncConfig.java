package com.dhflour.dhflourdemo1.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 스프링의 비동기 기능을 활성화
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 내부적으로 실행할 작업을 관리하는 스레드 풀을 제공
        executor.setCorePoolSize(10);
        // 코어 풀 사이즈는 풀이 관리하는 최소한의 스레드 수입니다.
        executor.setMaxPoolSize(100);
        // 최대 풀 사이즈는 풀이 필요에 따라 생성할 수 있는 최대 스레드 수입니다.
        executor.setQueueCapacity(500);
        // 큐 용량은 스레드 풀이 바쁜 경우 대기 중인 작업을 저장할 수 있는 큐의 최대 길이입니다.
        executor.initialize();
        // 이 호출은 스레드 풀을 활성화하기 전에 필요합니다.
        return executor; // 설정된 스레드 풀 실행자를 반환합니다.
    }
}
