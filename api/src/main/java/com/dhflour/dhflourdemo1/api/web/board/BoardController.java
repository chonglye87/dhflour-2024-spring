package com.dhflour.dhflourdemo1.api.web.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import com.dhflour.dhflourdemo1.api.domain.board.RBoardPaginationResponse;
import com.dhflour.dhflourdemo1.api.domain.board.RequestRBoard;
import com.dhflour.dhflourdemo1.api.domain.boardcategory.RBoardToCategory;
import com.dhflour.dhflourdemo1.api.service.board.BoardAPIService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import com.dhflour.dhflourdemo1.core.types.error.NoContentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping(value = "/api/v1/board")
@Tag(name = "게시판 API", description = "공지사항 대한 API")
@RestController
public class BoardController {

    @Autowired
    private BoardAPIService boardAPIService;

    @Operation(summary = "[board-1] 게시판 페이지 조회 (Pagination)",
            description = "게시판 목록 조회합니다.",
            operationId = "pageBoard", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = "created_at,desc", schema = @Schema(implementation = String.class)),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RBoardPaginationResponse.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getBoardCategories(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                      @Parameter(hidden = true) @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> boardAPIService.page(pageable))
                .switchIfEmpty(Mono.error(new NoContentException()))
                .flatMap(pageData -> Mono.just(new RBoardPaginationResponse(pageData)));
    }

    @Operation(summary = "[board-2] 게시판 상세 조회 (Get by ID)",
            description = "게시판를 ID로 조회합니다.",
            operationId = "getBoardById", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RBoard.class)))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getBoardById(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> boardAPIService.getById(id));
    }

    @Operation(summary = "[board-3] 게시판 생성 (Create)",
            description = "새로운 게시판를 생성합니다.",
            operationId = "createBoard", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "성공적으로 데이터가 생성됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RBoard.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<RBoard>> createBoard(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                                    @RequestBody RequestRBoard requestBody) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> boardAPIService.create(requestBody.toEntity(), requestBody.getCategoryIds()))
                .map(createdEntity -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createdEntity))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "[board-4] 게시판 수정 (Update)",
            description = "기존 게시판를 수정합니다.",
            operationId = "updateBoard", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 requestBody가 수정됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RequestRBoard.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> updateBoard(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                               @PathVariable Long id,
                               @RequestBody RequestRBoard requestBody) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> boardAPIService.update(id, requestBody.toEntity(), requestBody.getCategoryIds()));
    }

    @Operation(summary = "[board-5] 게시판 삭제 (Delete)",
            description = "기존 게시판를 삭제합니다.",
            operationId = "deleteBoard", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "성공적으로 데이터가 삭제됨")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> deleteBoard(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                               @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> boardAPIService.delete(id));
    }
}
