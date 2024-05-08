package com.dhflour.dhflourdemo1.core.service.process;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProcessEnvServiceImpl implements ProcessEnvService{

    @Value("${process.env}")
    private String processEnv;

    @Override
    public String getEnv() {
        return processEnv;
    }

    @Override
    public boolean isLocal() {
        return StringUtils.hasText(processEnv) && processEnv.equals("local");
    }

    @Override
    public boolean isDevelopment() {
        return StringUtils.hasText(processEnv) && processEnv.equals("development");
    }

    @Override
    public boolean isProduction() {
        return StringUtils.hasText(processEnv) && processEnv.equals("production");
    }
}
