package com.dhflour.dhflourdemo1.api.config;

import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
@EnableTransactionManagement
public class R2dbcConfig implements ReactiveAuditorAware<Long> {

    /**
     * 등록/수정 사용자 계정 access token 으로 추출
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ConnectionFactory는 R2DBC를 통해 데이터베이스에 연결하기 위한 팩토리입니다.
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory); // DatabaseClient를 생성합니다.
    }

    // R2dbcEntityTemplate은 R2DBC 엔티티의 데이터베이스 작업을 간단하게 처리할 수 있도록 도와줍니다.
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(DatabaseClient databaseClient) {
        return new R2dbcEntityTemplate(databaseClient.getConnectionFactory());
    }

    // R2dbcTransactionManager는 R2DBC 트랜잭션 관리를 담당합니다.
    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        log.debug("r2dbcTransactionManager");
        return new R2dbcTransactionManager(connectionFactory);
    }

    // ConnectionFactoryInitializer는 데이터베이스 초기화를 수행합니다.
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        // 데이터베이스 스키마를 초기화하는 데 사용됩니다.
        // initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }
}
