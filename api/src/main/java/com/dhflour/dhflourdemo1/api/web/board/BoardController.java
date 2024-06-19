package com.dhflour.dhflourdemo1.api.web.board;

import com.dhflour.dhflourdemo1.api.types.board.BoardPaginationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping(value = "/api/v1/board")
@Tag(name = "게시판 API", description = "공지사항 대한 API")
@RestController
public class BoardController {

    @Operation(summary = "[board-1] 게시판 목록 조회 (Pagination)",
            description = "게시물 페이징 목록 조회합니다.",
            operationId = "pageBoard", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = "", schema = @Schema(implementation = String.class)),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BoardPaginationResponse.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getAuthenticatedUserInfo(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails) {
        log.info("getAuthenticatedUserInfo: {}", userDetails);
        return userDetails;
    }
}
