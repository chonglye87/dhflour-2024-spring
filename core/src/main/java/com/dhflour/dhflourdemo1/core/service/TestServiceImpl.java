package com.dhflour.dhflourdemo1.core.service;

import com.dhflour.dhflourdemo1.core.service.setting.process.ProcessEnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ProcessEnvService processEnv;

    @Override
    public String test() {
        return "isLocal : " + processEnv.isLocal();
    }
}
