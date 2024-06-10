//package com.dhflour.dhflourdemo1.batch.dataloader;
//
//import com.dhflour.dhflourdemo1.jpa.domain.push.PushNotificationEntity;
//import com.dhflour.dhflourdemo1.jpa.domain.push.PushNotificationEntityRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@Order(2)
//public class PushExampleDataLoader implements CommandLineRunner {
//
//    @Autowired
//    private PushNotificationEntityRepository repository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        log.debug("::: BoardDataLoader:Run :::");
//
//        if (repository.findAll().isEmpty()) {
//            for (int i = 0; i < 100; i++) {
//                this.create("message : " + i, "recipient : " + i);
//            }
//        }
//    }
//
//    private PushNotificationEntity create(String message, String recipient) {
//        PushNotificationEntity pushNotificationEntity = new PushNotificationEntity();
//        pushNotificationEntity.setMessage(message);
//        pushNotificationEntity.setRecipient(recipient);
//        return repository.save(pushNotificationEntity);
//    }
//}
