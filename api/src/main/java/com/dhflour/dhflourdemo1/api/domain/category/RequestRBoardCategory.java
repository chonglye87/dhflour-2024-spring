package com.dhflour.dhflourdemo1.api.domain.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestRBoardCategory {

    @Schema(description = "카테고리명", requiredMode = Schema.RequiredMode.REQUIRED, example = "공지")
    private String name;

    public RBoardCategory toEntity() {
        RBoardCategory boardCategory = new RBoardCategory();
        boardCategory.setName(name);
        return boardCategory;
    }
}
