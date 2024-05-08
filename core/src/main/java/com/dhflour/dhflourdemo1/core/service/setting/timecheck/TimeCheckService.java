package com.dhflour.dhflourdemo1.core.service.setting.timecheck;

import com.dhflour.dhflourdemo1.core.types.process.TimeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class TimeCheckService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TimeInfo getTimeInfo() {
        ZonedDateTime serverTime = ZonedDateTime.now();
        String serverTimeZone = serverTime.getZone().toString();

        // Only Mysql
        LocalDateTime dbLocalDateTime = jdbcTemplate.queryForObject("SELECT now()", LocalDateTime.class);
        String dbTimeZone = jdbcTemplate.queryForObject("SELECT @@time_zone", String.class);
        String dbSystemTimeZone = jdbcTemplate.queryForObject("SELECT @@system_time_zone", String.class);

        return new TimeInfo(serverTime.toLocalDateTime(), serverTimeZone, dbLocalDateTime, dbTimeZone, dbSystemTimeZone);
    }
}
