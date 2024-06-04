package com.dhflour.dhflourdemo1.core.service.setting.process;

public interface ProcessEnvService {

    String getEnv();

    boolean isLocal();

    boolean isDevelopment();

    boolean isProduction();
}
