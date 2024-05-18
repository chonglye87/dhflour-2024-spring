package com.dhflour.dhflourdemo1.batch.service;

import com.dhflour.dhflourdemo1.core.service.notification.NotificationPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationPushService notificationPushService;

    @Override
    public void send(String value) {
        notificationPushService.send(value);
    }
}
