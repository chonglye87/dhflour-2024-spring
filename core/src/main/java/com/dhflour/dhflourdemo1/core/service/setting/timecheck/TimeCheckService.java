package com.dhflour.dhflourdemo1.core.service.setting.timecheck;

import com.dhflour.dhflourdemo1.core.types.process.TimeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class TimeCheckService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TimeInfo getTimeInfo() {
        TimeInfo timeInfo = new TimeInfo();
        ZonedDateTime serverTime = ZonedDateTime.now();
        timeInfo.setServerTime(serverTime.toLocalDateTime());
        timeInfo.setServerTimeZone(serverTime.getZone().toString());

        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource != null) {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                log.debug("Database Product Name: " + metaData.getDatabaseProductName());
                log.debug("Database Product Version: " + metaData.getDatabaseProductVersion());
                log.debug("JDBC Driver Name: " + metaData.getDriverName());
                log.debug("JDBC Driver Version: " + metaData.getDriverVersion());
                if (metaData.getDatabaseProductName() != null && metaData.getDatabaseProductName().equals("MySQL")) {
                    LocalDateTime dbLocalDateTime = jdbcTemplate.queryForObject("SELECT now()", LocalDateTime.class);
                    timeInfo.setDbTime(dbLocalDateTime);
                    String dbTimeZone = jdbcTemplate.queryForObject("SELECT @@time_zone", String.class);
                    timeInfo.setDbTimeZone(dbTimeZone);
                    String dbSystemTimeZone = jdbcTemplate.queryForObject("SELECT @@system_time_zone", String.class);
                    timeInfo.setDbSystemTimeZone(dbSystemTimeZone);
                }
            } catch (SQLException e) {
                System.err.println("Error getting database metadata: " + e.getMessage());
            }
        }

        return timeInfo;
    }
}
