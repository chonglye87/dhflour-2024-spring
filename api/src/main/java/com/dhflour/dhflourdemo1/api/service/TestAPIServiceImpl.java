package com.dhflour.dhflourdemo1.api.service;

import com.dhflour.dhflourdemo1.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestAPIServiceImpl implements TestAPIService {

    @Autowired
    private TestService testService;

    @Override
    public String test() {
        return "API Test Service";
    }
}
