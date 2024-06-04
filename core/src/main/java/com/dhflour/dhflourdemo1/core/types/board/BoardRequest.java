package com.dhflour.dhflourdemo1.core.types.board;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 등록용은 DTO 를 필수적으로 만들어야함.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {

    //    @Schema(description = "게시물 제목", required = true, example = "Spring Boot and Swagger Integration")
    private String title;

    //    @Schema(description = "게시물 내용", required = true, example = "Here is how you can integrate Swagger into your Spring Boot application...")
    private String content;

    //    @Schema(description = "카테고리 ID 목록", required = true, example = "[1, 2, 3]")
    private Set<Long> categoryIds;
}
