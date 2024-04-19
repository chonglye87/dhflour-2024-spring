package com.dhflour.dhflourdemo1.core.service;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test() {
        return "Core Test Service";
    }
}
