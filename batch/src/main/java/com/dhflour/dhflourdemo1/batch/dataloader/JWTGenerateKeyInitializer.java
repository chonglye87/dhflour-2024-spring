package com.dhflour.dhflourdemo1.batch.dataloader;

import com.dhflour.dhflourdemo1.core.service.jwt.JWTAsymmetricService;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Component
public class JWTGenerateKeyInitializer implements CommandLineRunner {

    @Autowired
    private JWTSymmetricService jwtSymmetricService;

    @Autowired
    private JWTAsymmetricService jwtAsymmetricService;

    @Override
    public void run(String... args) throws Exception {
        jwtSymmetricService.createSecretKey();
        jwtAsymmetricService.createSecretKey();
    }
}
