package com.dhflour.dhflourdemo1.core.types.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInfo {
    private LocalDateTime serverTime;
    private String serverTimeZone;
    private String dbProductName;
    private LocalDateTime dbTime;
    private String dbTimeZone;
    private String dbSystemTimeZone;
}
