package com.dhflour.dhflourdemo1.core.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Value("${dhflour.id}")
    private String id;

    @Override
    public String getText() {
        return id;
    }
}
