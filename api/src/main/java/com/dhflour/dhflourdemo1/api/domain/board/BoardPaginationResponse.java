package com.dhflour.dhflourdemo1.api.domain.board;


import com.dhflour.dhflourdemo1.api.types.pagination.PaginationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "게시판 페이지네이션 응답")
public class BoardPaginationResponse extends PaginationResponse<RBoard> {

    public BoardPaginationResponse(Page<RBoard> pageData) {
        super(pageData);
    }
}