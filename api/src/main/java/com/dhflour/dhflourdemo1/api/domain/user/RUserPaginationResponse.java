package com.dhflour.dhflourdemo1.api.domain.user;


import com.dhflour.dhflourdemo1.api.types.pagination.PaginationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "사용자 페이지네이션 응답")
public class RUserPaginationResponse extends PaginationResponse<RUser> {

    public RUserPaginationResponse(Page<RUser> pageData) {
        super(pageData);
    }
}