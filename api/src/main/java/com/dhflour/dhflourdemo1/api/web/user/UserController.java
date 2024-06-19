package com.dhflour.dhflourdemo1.api.web.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/v1/user")
@Tag(name = "회원 API", description = "회원가입 및 회원정보 조회에 대한 API")
@RestController
public class UserController {

}
