package com.dhflour.dhflourdemo1.api.domain.board;

import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class RBoardRepositoryImpl implements RBoardRepositoryCustom {


    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Flux<RBoard> findAllByFilter(PageFilter pageFilter, List<Long> categoryIds) {
        StringBuilder sql = new StringBuilder("SELECT b.* FROM board b ");

        // Join with board_category and category tables
        if (categoryIds != null && !categoryIds.isEmpty()) {
            sql.append("JOIN board_category bc ON b.id = bc.board_id ")
                    .append("JOIN category c ON bc.category_id = c.id ");
        }

        sql.append("WHERE 1=1 ");

        // query 조건 추가
        if (pageFilter.getQuery() != null && !pageFilter.getQuery().isEmpty()) {
            sql.append("AND (b.title LIKE '%" + pageFilter.getQuery() + "%' OR b.content LIKE '%" + pageFilter.getQuery() + "%') ");
        }

        // startTime 조건 추가
        if (pageFilter.getStartDateTime() != null) {
            LocalDateTime startDateTime = pageFilter.getStartDateTime().withHour(0).withMinute(0).withSecond(0);
            sql.append("AND b.created_at >= '").append(startDateTime.format(PageFilter.FORMATTER)).append("' ");
        }

        // endTime 조건 추가
        if (pageFilter.getEndDateTime() != null) {
            LocalDateTime endDateTime = pageFilter.getEndDateTime().withHour(23).withMinute(59).withSecond(59);
            sql.append("AND b.created_at <= '").append(endDateTime.format(PageFilter.FORMATTER)).append("' ");
        }

        // categoryIds 조건 추가
        if (categoryIds != null && !categoryIds.isEmpty()) {
            sql.append("AND c.id IN (");
            for (int i = 0; i < categoryIds.size(); i++) {
                sql.append(categoryIds.get(i));
                if (i < categoryIds.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") ");
        }

        // pagination 추가
        sql.append("ORDER BY b.")
                .append(pageFilter.getPageable().getSort().toString().replace(":", " "))
                .append(" LIMIT ")
                .append(pageFilter.getPageable().getPageSize())
                .append(" OFFSET ")
                .append(pageFilter.getPageable().getOffset());

        return databaseClient.sql(sql.toString())
                .map((row, metadata) -> {
                    RBoard board = new RBoard();
                    board.setId(row.get("id", Long.class));
                    board.setTitle(row.get("title", String.class));
                    board.setContent(row.get("content", String.class));
                    board.setCreatedAt(row.get("created_at", LocalDateTime.class));
                    // other properties
                    return board;
                })
                .all();
    }
}
