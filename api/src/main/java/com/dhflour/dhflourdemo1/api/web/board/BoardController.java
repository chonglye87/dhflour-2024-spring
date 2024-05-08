package com.dhflour.dhflourdemo1.api.web.board;

import com.dhflour.dhflourdemo1.api.service.board.BoardAPIService;
import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.core.service.board.BoardService;
import com.dhflour.dhflourdemo1.core.service.category.CategoryService;
import com.dhflour.dhflourdemo1.core.types.board.BoardPaginationResponse;
import com.dhflour.dhflourdemo1.core.types.board.BoardRequest;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    @Autowired
    private BoardAPIService boardAPIService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "[board-1] 게시판 목록 조회 (Pagination)",
            description = "게시판 목록 조회 API (Pagination)",
            operationId = "pageBoard")
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20"),
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0"),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = ""),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardPaginationResponse.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> page(@Parameter(hidden = true) @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(required = false, defaultValue = "") String startDate,
                                  @RequestParam(required = false, defaultValue = "") String endDate,
                                  @RequestParam(required = false, defaultValue = "") String query,
                                  Locale locale) {

        PageFilter filter = new PageFilter.Builder()
                .pageable(pageable)
                .query(query)
                .startDate(startDate)
                .endDate(endDate)
                .locale(locale)
                .build();
        ;
        return ResponseEntity.ok(new BoardPaginationResponse(boardService.page(filter)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[board-2] 게시판 등록",
            description = "새로운 게시글을 등록합니다.",
            operationId = "createBoard")
    @ApiResponse(responseCode = "201", description = "게시글이 성공적으로 등록됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardRequest.class)))
    public ResponseEntity<BoardEntity> createBoard(@RequestBody BoardRequest payload) {
        BoardEntity createdBoard = boardService.create(payload.toEntity(categoryService));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }
}
