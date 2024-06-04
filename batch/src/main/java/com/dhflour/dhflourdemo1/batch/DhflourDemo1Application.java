package com.dhflour.dhflourdemo1.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {
        "com.dhflour.dhflourdemo1.batch",
        "com.dhflour.dhflourdemo1.jpa",
        "com.dhflour.dhflourdemo1.core",
})
@EnableJpaRepositories(
        basePackages = {"com.dhflour.dhflourdemo1.jpa.domain"},
        transactionManagerRef = "jpaTransactionManager"
)
@EntityScan({"com.dhflour.dhflourdemo1.jpa.domain"})
public class DhflourDemo1Application {

    public static void main(String[] args) {
        System.out.println("\uD83D\uDE80 BatchApplication starting \uD83D\uDE80");

        // java -jar your-application.jar --spring.profiles.active=prod // 운영실행방법
        String active = System.getProperty("spring.profiles.active");
        if (!StringUtils.hasText(active)) {
            System.setProperty("spring.profiles.active", "local"); // properties 설정
        }
        Properties p = System.getProperties();
        Enumeration keys = p.keys();

        System.out.println("--------------------");
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) p.get(key);
            System.out.println("• Property : [" + key + " = " + value + "]");
        }
        System.out.println("--------------------");

        SpringApplication.run(DhflourDemo1Application.class, args);
//        SpringApplication app = new SpringApplication(DhflourDemo1Application.class);
//        app.setAdditionalProfiles("api"); // 프로필 프로그래밍 방식으로 설정
//        app.run(args);
    }

}
