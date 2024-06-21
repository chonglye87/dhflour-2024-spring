package com.dhflour.dhflourdemo1.api.domain.user;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RUserRepositoryImpl implements RUserRepositoryCustom {


    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Flux<RUser> findAllByFilter(PageFilter pageFilter) {
        StringBuilder sql = new StringBuilder("SELECT u.* FROM user u ");

        sql.append("WHERE 1=1 ");

        // query 조건 추가
        if (pageFilter.getQuery() != null && !pageFilter.getQuery().isEmpty()) {
            sql.append("AND (u.username LIKE '%" + pageFilter.getQuery() + "%' OR u.email LIKE '%" + pageFilter.getQuery() + "%') ");
        }

        // startTime 조건 추가
        if (pageFilter.getStartDateTime() != null) {
            LocalDateTime startDateTime = pageFilter.getStartDateTime().withHour(0).withMinute(0).withSecond(0);
            sql.append("AND u.created_at >= '").append(startDateTime.format(PageFilter.FORMATTER)).append("' ");
        }

        // endTime 조건 추가
        if (pageFilter.getEndDateTime() != null) {
            LocalDateTime endDateTime = pageFilter.getEndDateTime().withHour(23).withMinute(59).withSecond(59);
            sql.append("AND u.created_at <= '").append(endDateTime.format(PageFilter.FORMATTER)).append("' ");
        }

        // pagination 추가
        sql.append("ORDER BY u.")
                .append(pageFilter.getPageable().getSort().toString().replace(":", " "))
                .append(" LIMIT ")
                .append(pageFilter.getPageable().getPageSize())
                .append(" OFFSET ")
                .append(pageFilter.getPageable().getOffset());

        return databaseClient.sql(sql.toString())
                .map((row, metadata) -> {
                    RUser user = new RUser();
                    user.setId(row.get("id", Long.class));
                    user.setUsername(row.get("username", String.class));
                    user.setEmail(row.get("email", String.class));
                    user.setMobile(row.get("mobile", String.class));
                    user.setPolicy(row.get("policy", Boolean.class));
                    user.setPrivacy(row.get("privacy", Boolean.class));
                    user.setMarketing(row.get("marketing", Boolean.class));
                    user.setCreatedAt(row.get("created_at", LocalDateTime.class));
                    return user;
                })
                .all();
    }
}
