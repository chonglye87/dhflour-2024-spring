# Spring Observability
## 서론
Spring Observability 팀은 오랜 기간 동안 Spring 애플리케이션에 대한 관측 지원을 추가해 왔으며, 이제 Spring Framework 6와 Spring Boot 3에서 이를 일반적으로 사용할 수 있게 되었습니다. 관측 가능성은 시스템의 출력을 통해 내부를 얼마나 잘 이해할 수 있는지를 의미합니다. Spring Boot 3.0.0-RC1에는 Micrometer와 Micrometer Tracing을 통한 개선된 메트릭과 분산 추적 지원이 포함됩니다. 주요 변경 사항으로는 로그 상관관계 지원, W3C 컨텍스트 전파 기본 설정, 원격 배지("remote baggage") 메타데이터 자동 전파 지원이 있습니다. 또한, 새로운 Observation API를 도입하여 사용자들이 단일 API를 통해 코드 계측을 한번에 수행하고 여러 이점을 누릴 수 있도록 했습니다.

## Micrometer 관측이 작동
Micrometer 관측이 작동하려면 ObservationRegistry를 통해 ObservationHandler 객체를 등록해야 합니다. ObservationHandler는 Observation.Context의 생명주기 이벤트(시작, 중지, 오류, 이벤트 발생, 범위 시작 및 중지)에 반응하여 타이머, 스팬, 로그 등을 생성합니다. 관측 중 추가 메타데이터(태그)로 프로덕션 문제를 디버그할 수 있습니다.

다음은 Micrometer Observation API의 예제입니다:

```java
ObservationRegistry registry = ObservationRegistry.create();
registry.observationConfig().observationHandler(new MyHandler());

Observation.createNotStarted("user.name", registry)
        .contextualName("getting-user-name")
        .lowCardinalityKeyValue("userType", "userType1")
        .highCardinalityKeyValue("userId", "1234")
        .observe(() -> log.info("Hello"));
```

높은 카디널리티는 무한한 가능한 값을 가지는 키-값 쌍을 의미하고, 낮은 카디널리티는 제한된 값을 가지는 것을 의미합니다.

## 첫 번째 관측 애플리케이션을 만들려면
첫 번째 관측 애플리케이션을 만들려면 https://start.spring.io에서 새 프로젝트를 생성하는 것이 가장 쉽습니다. Spring Boot 3.0.0-SNAPSHOT을 선택하고(사용 가능 시 RC1으로 전환), 선호하는 빌드 도구를 선택하세요. Spring WebMvc 서버 애플리케이션과 RestTemplate을 사용하는 클라이언트를 구축할 것입니다. 서버 측부터 시작합니다.

## WebMvc 서버 설정 요약

1. **프로젝트 설정**:
    - `org.springframework.boot:spring-boot-starter-web` 의존성 추가 (HTTP 서버 시작).
    - `org.springframework.boot:spring-boot-starter-aop` 의존성 추가 (관측 생성).
    - `spring-boot-starter-actuator` 추가 (Micrometer 추가).

2. **관측 기능 추가**:
    - **메트릭**: `io.micrometer:micrometer-registry-prometheus` 추가.
    - **추적**: `io.micrometer:micrometer-tracing-bridge-brave` 추가 (Zipkin Brave 사용).
    - **로그**: `com.github.loki4j:loki-logback-appender` 추가 (Grafana Loki 사용).

3. **설정 파일 추가**:
    - `application.properties` 
    ```
    server.port=6543
    spring.application.name=client
    
    # All traces should be sent to latency analysis tool
    management.tracing.sampling.probability=1.0
    management.endpoints.web.exposure.include=prometheus
    
    # traceID and spanId are predefined MDC keys - we want the logs to include them
    logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
    ```
    - `/src/main/resources/logback-spring.xml` 
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <configuration>
        <include resource="org/springframework/boot/logging/logback/base.xml" />
        <springProperty scope="context" name="appName" source="spring.application.name"/>
    
        <appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender">
            <http>
                <url>http://localhost:3100/loki/api/v1/push</url>
            </http>
            <format>
                <label>
                    <pattern>app=${appName},host=${HOSTNAME},traceID=%X{traceId:-NONE},level=%level</pattern>
                </label>
                <message>
                    <pattern>${FILE_LOG_PATTERN}</pattern>
                </message>
                <sortByTime>true</sortByTime>
            </format>
        </appender>
    
        <root level="INFO">
            <appender-ref ref="LOKI"/>
        </root>
    </configuration>
    ```