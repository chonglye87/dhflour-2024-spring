package com.dhflour.dhflourdemo1.api.service.test;

import java.util.stream.LongStream;

public class CpuIntensiveTask {


    public static void performCpuIntensiveTask() {

        LongStream.range(Long.MAX_VALUE / 2, Long.MAX_VALUE)
                .parallel()
                .filter(CpuIntensiveTask::isPrime)
                .findAny();

    }

    private static boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
