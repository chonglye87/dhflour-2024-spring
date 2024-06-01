package com.dhflour.dhflourdemo1.api.web.user;

import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.core.service.user.UserService;
import com.dhflour.dhflourdemo1.core.types.board.BoardRequest;
import com.dhflour.dhflourdemo1.core.types.user.JoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "[user-1] 회원가입",
            description = "유저정보를 등록합니다.",
            operationId = "joinUser")
    @ApiResponse(responseCode = "201", description = "회원가입을 성공적으로 등록됨",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserEntity.class)))
    public ResponseEntity<UserEntity> join(@RequestBody JoinRequest request) {
        UserEntity userEntity = request.toEntity(passwordEncoder);
        UserEntity createdUser = userService.create(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // TODO 과제
    // TODO 페이지네이션 API GET /
    // TODO 상세보기 API GET /{id}
    // TODO 등록하기 API POST /
    // TODO 수정하기 API PUT /
    // TODO 삭제하기 API DELETE /
}
