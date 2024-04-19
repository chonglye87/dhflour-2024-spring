package com.dhflour.dhflourdemo1.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {
        "com.dhflour.dhflourdemo1.api",
        "com.dhflour.dhflourdemo1.core",
})
public class DhflourDemo1Application {

    public static void main(String[] args) {
        System.out.println("=== START APPLICATION ===");
        ApplicationContext ctx = SpringApplication.run(DhflourDemo1Application.class, args);

        // ApplicationContext에 등록된 모든 빈의 이름을 가져와 출력
//        System.out.println("==== List of Beans registered in Spring Boot's ApplicationContext ====");
//        String[] beanNames = ctx.getBeanDefinitionNames(); // 모든 빈 이름을 가져옴
//        for (String beanName : beanNames) {
//            System.out.println(": " + beanName); // 빈 이름 출력
//        }
    }
}
