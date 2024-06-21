package com.dhflour.dhflourdemo1.api.domain.board;


import com.dhflour.dhflourdemo1.api.types.pagination.PaginationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "게시판 페이지네이션 응답")
public class RBoardPaginationResponse extends PaginationResponse<RBoard> {

    public RBoardPaginationResponse(Page<RBoard> pageData) {
        super(pageData);
    }
}