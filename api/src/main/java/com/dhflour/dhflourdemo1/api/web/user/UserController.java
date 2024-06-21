package com.dhflour.dhflourdemo1.api.web.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserPaginationResponse;
import com.dhflour.dhflourdemo1.api.domain.user.RequestRUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestUpdatePassword;
import com.dhflour.dhflourdemo1.api.service.user.UserManageService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.api.utils.AuthUtils;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.ForbiddenException;
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
@RequestMapping(value = "/api/v1/user")
@Tag(name = "회원 API", description = "회원가입 및 회원정보 조회에 대한 API")
@RestController
public class UserController {

    @Autowired
    private UserManageService userManageService;

    @Operation(summary = "[user-1] 사용자 페이지 조회 (Pagination)",
            description = "사용자 목록 조회합니다.",
            operationId = "pageUser", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameters(value = {
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0", schema = @Schema(implementation = Integer.class)),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = "created_at,desc", schema = @Schema(implementation = String.class)),
    })
    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RUserPaginationResponse.class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> pageUser(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                            @Parameter(hidden = true) @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userManageService.page(pageable))
                .switchIfEmpty(Mono.error(new NoContentException()))
                .flatMap(pageData -> Mono.just(new RUserPaginationResponse(pageData)));
    }

    @Operation(summary = "[user-2] 사용자 상세 조회 (Get by ID)",
            description = "사용자를 ID로 조회합니다.",
            operationId = "getUserById", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 불러옴",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RUser.class)))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getUserById(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                               @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userManageService.getById(id));
    }

    @Operation(summary = "[user-3] 사용자 생성 (Create)",
            description = "새로운 사용자를 생성합니다.",
            operationId = "createUser", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "성공적으로 데이터가 생성됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RUser.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<RUser>> createUser(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                                  @RequestBody RequestRUser requestBody) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userManageService.create(requestBody.toEntity()))
                .map(createdEntity -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createdEntity))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "[user-4] 사용자 수정 (Update)",
            description = "기존 사용자를 수정합니다.",
            operationId = "updateUser", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 requestBody가 수정됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RequestRUser.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> updateUser(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                              @PathVariable Long id,
                              @RequestBody RequestRUser requestBody) {

        return AuthUtils.required(userDetails)
                .filter(details -> AuthUtils.hasRole(details, "ROLE_USER"))
                .switchIfEmpty(Mono.error(new ForbiddenException()))
                .flatMap(details -> userManageService.checkUnique(requestBody, id)
                        .filter(isUnique -> isUnique)
                        .switchIfEmpty(Mono.error(new BadRequestException())))
                .flatMap(isUnique -> userManageService.update(id, requestBody.toEntity()));
    }

    @Operation(summary = "[user-5] 사용자 삭제 (Delete)",
            description = "기존 사용자를 삭제합니다.",
            operationId = "deleteUser", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "성공적으로 데이터가 삭제됨")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> deleteUser(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                              @PathVariable Long id) {
        return AuthUtils.required(userDetails)
                .flatMap(user -> userManageService.delete(id));
    }

    @Operation(summary = "[user-6] 비밀번호 수정 (Update Password)",
            description = "사용자의 비밀번호를 수정합니다.",
            operationId = "updatePassword", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "성공적으로 비밀번호가 수정됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RequestUpdatePassword.class)))
    @PutMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> updatePassword(@AuthenticationPrincipal Mono<ReactiveUserDetails> userDetails,
                                  @PathVariable Long id,
                                  @RequestBody RequestUpdatePassword request) {

        return AuthUtils.required(userDetails)
                .filter(details -> AuthUtils.hasRole(details, "ROLE_USER"))
                .switchIfEmpty(Mono.error(new ForbiddenException()))
                .flatMap(details -> userManageService.updatePassword(id, request.getOldPassword(), request.getNewPassword()));
    }
}
