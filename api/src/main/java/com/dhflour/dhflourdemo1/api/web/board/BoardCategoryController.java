package com.dhflour.dhflourdemo1.api.web.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoardPaginationResponse;
import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import com.dhflour.dhflourdemo1.api.domain.category.RequestRBoardCategory;
import com.dhflour.dhflourdemo1.api.service.category.CategoryAPIService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping(value = "/api/v1/board-category")
@Tag(name = "게시판 API", description = "공지사항 대한 API")
@RestController
public class BoardCategoryController {

    @Autowired
    private CategoryAPIService categoryAPIService;

    @Operation(summary = "[board-category-1] 게시판 카테고리 목록 조회 (List)",
            description = "게시물 카테고리 목록 조회합니다.",
            operationId = "listBoardCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = "created_at,desc", schema = @Schema(implementation = String.class)),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = RBoardCategory.class))))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<?> getBoardCategories(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                      @Parameter(hidden = true) Sort sort) {
        return AuthUtils.required(userDetails)
                .flatMapMany(user -> categoryAPIService.list(sort));
    }

    @Operation(summary = "[board-category-2] 게시판 카테고리 상세 조회 (Get by ID)",
            description = "게시물 카테고리를 ID로 조회합니다.",
            operationId = "getBoardCategoryById", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 카테고리를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RBoardCategory.class)))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getBoardCategoryById(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                        @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> categoryAPIService.getById(id));
    }

    @Operation(summary = "[board-category-3] 게시판 카테고리 생성 (Create)",
            description = "새로운 게시물 카테고리를 생성합니다.",
            operationId = "createBoardCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "성공적으로 카테고리가 생성됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RequestRBoardCategory.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<RBoardCategory>> createBoardCategory(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                                                    @RequestBody RequestRBoardCategory category) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> categoryAPIService.create(category.toEntity()))
                .map(createdEntity -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createdEntity))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "[board-category-4] 게시판 카테고리 수정 (Update)",
            description = "기존 게시물 카테고리를 수정합니다.",
            operationId = "updateBoardCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 카테고리가 수정됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RequestRBoardCategory.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> updateBoardCategory(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                       @PathVariable Long id,
                                       @RequestBody RequestRBoardCategory category) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> categoryAPIService.update(id, category.toEntity()));
    }

    @Operation(summary = "[board-category-5] 게시판 카테고리 삭제 (Delete)",
            description = "기존 게시물 카테고리를 삭제합니다.",
            operationId = "deleteBoardCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "성공적으로 카테고리가 삭제됨")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> deleteBoardCategory(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                       @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> categoryAPIService.delete(id));
    }
}
