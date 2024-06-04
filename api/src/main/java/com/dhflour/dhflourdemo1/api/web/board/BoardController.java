package com.dhflour.dhflourdemo1.api.web.board;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "[board-1] 게시판 목록 조회 (Pagination)",
            description = "게시물 페이징 목록 조회합니다.",
            operationId = "pageBoard")
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = "", schema = @Schema(implementation = String.class)),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardPaginationResponse.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> page(@Parameter(hidden = true) @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
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

        Page<BoardEntity> page = boardService.page(filter);
        return page != null ? Mono.just(new BoardPaginationResponse(page)) : Mono.empty();
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

    // ==============================
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[board-5] 게시판 수정",
            description = "ID의 게시글을 수정합니다.",
            operationId = "updateBoard")
    @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 수정됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardRequest.class)))
    public ResponseEntity<BoardEntity> updateBoard(@PathVariable Long id,
                                                   @RequestBody BoardRequest payload) {
        BoardEntity board = payload.toEntity(categoryService);
        board.setId(id);
        BoardEntity updatedBoard = boardService.update(board);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBoard);
    }
    // ==============================

    // GET /api/v1/board/{id}
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[board-3] 게시판 상세 조회",
            description = "특정 게시글의 상세 정보를 조회합니다.",
            operationId = "getBoardById")
    @ApiResponse(responseCode = "200", description = "게시글 상세 정보 반환",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardEntity.class)))
    public ResponseEntity<BoardEntity> getBoardById(@PathVariable Long id,
                                                    Locale locale) {
        BoardEntity board = boardService.get(locale, id);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[board-4] 게시판 삭제",
            description = "특정 게시글을 삭제합니다.",
            operationId = "deleteBoard")
    @ApiResponse(responseCode = "204", description = "게시글이 성공적으로 삭제됨")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id,
                                         Locale locale) {
        BoardEntity board = boardService.get(locale, id);
        if (board != null) {
            boardService.delete(board);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
