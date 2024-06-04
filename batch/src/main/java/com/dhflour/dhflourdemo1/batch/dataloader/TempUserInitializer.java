package com.dhflour.dhflourdemo1.batch.dataloader;

import com.dhflour.dhflourdemo1.jpa.domain.user.UserAgreementEmbedded;
import com.dhflour.dhflourdemo1.jpa.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.jpa.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TempUserInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userService.get(Locale.KOREA, 1L) == null) {
            UserEntity user = new UserEntity();
            user.setUsername("홍길동");
            user.setPassword("1234");
            user.setEmail("test@test.com");
            user.setMobile("01011110000");
            UserAgreementEmbedded userAgreementEmbedded = new UserAgreementEmbedded();
            userAgreementEmbedded.setMarketing(true);
            userAgreementEmbedded.setPolicy(true);
            userAgreementEmbedded.setPrivacy(true);
            user.setUserAgreement(userAgreementEmbedded);
            userService.create(user);
        }
    }
}