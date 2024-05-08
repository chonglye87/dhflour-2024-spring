package com.dhflour.dhflourdemo1.core.types.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInfo {
    private LocalDateTime serverTime;
    private String serverTimeZone;
    private LocalDateTime dbTime;
    private String dbTimeZone;
    private String dbSystemTimeZone;
}