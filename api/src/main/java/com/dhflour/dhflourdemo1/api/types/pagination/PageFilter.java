package com.dhflour.dhflourdemo1.api.types.pagination;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Builder
@Data
public class PageFilter implements Serializable {

    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private Locale locale;
    private Pageable pageable;
    private String query;
    private String startTime;
    private String endTime;

    public LocalDateTime getStartDateTime() {
        return startTime != null && !startTime.isEmpty() ? LocalDateTime.parse(startTime, FORMATTER) : null;
    }

    public LocalDateTime getEndDateTime() {
        return endTime != null && !endTime.isEmpty() ? LocalDateTime.parse(endTime, FORMATTER) : null;
    }
}
