package com.dhflour.dhflourdemo1.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {
        "com.dhflour.dhflourdemo1.api",
        "com.dhflour.dhflourdemo1.core",
})
public class DhflourDemo1Application {

    public static void main(String[] args) {
        System.out.println("\uD83D\uDE80 ApiApplication started successfully");
        String profiles = System.getProperty("spring.profiles.default");

        if (!StringUtils.hasText(profiles)) {
            System.setProperty("spring.profiles.default", "api"); // properties 설정
        }
        System.out.printf("• spring.profiles.default : [%s]%n", System.getProperty("spring.profiles.default"));

//        SpringApplication.run(Server.class, args);
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
