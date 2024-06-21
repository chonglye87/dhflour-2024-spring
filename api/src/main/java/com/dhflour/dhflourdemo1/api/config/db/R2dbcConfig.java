package com.dhflour.dhflourdemo1.api.config.db;

import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.publisher.Mono;

/**
 * R2DBC( Reactive Relational Database Connectivity ) 설정을 담당하며, 데이터베이스 작업을 간단하게 처리하고,
 * 트랜잭션 관리를 지원한다.
 */
@Slf4j
@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
@EnableTransactionManagement
public class R2dbcConfig implements ReactiveAuditorAware<Long> {

    /**
     * 등록/수정 사용자 계정을 access token으로 추출하여 반환한다.
     *
     * @return 등록/수정 사용자 ID
     */
    @Override
    public Mono<Long> getCurrentAuditor() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    if (authentication == null || !authentication.isAuthenticated()) {
                        return 0L;
                    }
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof ReactiveUserDetails) {
                        return ((ReactiveUserDetails) principal).getId();
                    }
                    return 0L;
                });
    }

    /**
     * PasswordEncoder 빈을 생성한다.
     *
     * @return BCryptPasswordEncoder 단방향 암호화 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DatabaseClient 빈을 생성한다.
     *
     * @param connectionFactory 데이터베이스에 연결하기 위한 팩토리
     * @return DatabaseClient 인스턴스
     */
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory); // DatabaseClient를 생성합니다.
    }

    /**
     * R2dbcEntityTemplate 빈을 생성한다.
     *
     * @param databaseClient DatabaseClient 인스턴스
     * @return R2dbcEntityTemplate 인스턴스
     */
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(DatabaseClient databaseClient) {
        return new R2dbcEntityTemplate(databaseClient.getConnectionFactory());
    }

    /**
     * R2dbcTransactionManager 빈을 생성한다.
     *
     * @param connectionFactory 데이터베이스에 연결하기 위한 팩토리
     * @return R2dbcTransactionManager 인스턴스
     */
    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    /**
     * ConnectionFactoryInitializer 빈을 생성하여 데이터베이스 초기화를 수행한다.
     *
     * @param connectionFactory 데이터베이스에 연결하기 위한 팩토리
     * @return ConnectionFactoryInitializer 인스턴스
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        // 데이터베이스 스키마를 초기화하는 데 사용됩니다.
        // initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }
}





