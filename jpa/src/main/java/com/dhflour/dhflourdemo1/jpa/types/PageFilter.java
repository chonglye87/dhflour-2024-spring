package com.dhflour.dhflourdemo1.jpa.types;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PageFilter implements Serializable {

    private static final long serialVersionUID = 7986177657377217045L;

    public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public final static DateTimeFormatter FORMATTER_DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Locale locale;
    private Pageable pageable;
    private String query;
    private String startDate;
    private String endDate;

    private PageFilter(Builder builder) {
        this.locale = builder.locale;
        this.pageable = builder.pageable;
        this.query = builder.query;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public static class Builder {
        private Locale locale;
        private Pageable pageable;
        private String query;
        private String startDate;
        private String endDate;

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

        public Builder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(String endDate) {
            this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
