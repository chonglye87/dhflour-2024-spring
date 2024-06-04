package com.dhflour.dhflourdemo1.api.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;

// R2DBC 리포지토리를 활성화하고, 베이스 패키지를 지정합니다.
@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.dhflour.dhflourdemo1.api.domain",
        entityOperationsRef = "r2dbcEntityTemplate" // 기본 R2DBC 엔티티 템플릿을 지정합니다.
)
public class R2dbcConfig {

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
    public ReactiveTransactionManager r2dbcTransactionManager(ConnectionFactory connectionFactory) {
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