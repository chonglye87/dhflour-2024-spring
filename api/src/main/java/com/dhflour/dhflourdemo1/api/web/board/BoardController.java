package com.dhflour.dhflourdemo1.api.web.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/v1/board")
@Tag(name = "게시판 API", description = "공지사항 대한 API")
@RestController
public class BoardController {
}
