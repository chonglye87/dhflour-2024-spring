package com.dhflour.dhflourdemo1.jpa.types;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PageFilter implements Serializable {

    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
//    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    public final static DateTimeFormatter FORMATTER_DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Locale locale;
    private Pageable pageable;
    private String query;
    private String startTime;
    private String endTime;

    private PageFilter(Builder builder) {
        this.locale = builder.locale;
        this.pageable = builder.pageable;
        this.query = builder.query;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static class Builder {
        private Locale locale;
        private Pageable pageable;
        private String query;
        private String startTime;
        private String endTime;

        public Builder() {
            this.locale = Locale.KOREAN; // 기본값 설정
        }

        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder pageable(Pageable pageable) {
            this.pageable = pageable;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public PageFilter build() {
            return new PageFilter(this);
        }
    }

    // Getter 메서드
    public Locale getLocale() {
        return locale;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public String getQuery() {
        return query;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
