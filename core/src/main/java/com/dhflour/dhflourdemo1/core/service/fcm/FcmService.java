package com.dhflour.dhflourdemo1.core.service.fcm;

public interface FcmService {

    void sendNotification(String token, String title, String body);
}
