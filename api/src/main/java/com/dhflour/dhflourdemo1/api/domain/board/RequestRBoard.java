package com.dhflour.dhflourdemo1.api.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestRBoard {

    @Schema(description = "제목", requiredMode = Schema.RequiredMode.REQUIRED, example = "", maxLength = 255)
    private String title;

    @Schema(description = "내용", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String content;

    @Schema(description = "카테고리 ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private List<Long> categoryIds = new ArrayList<>();

    public RBoard toEntity() {
        RBoard entity = new RBoard();
        entity.setTitle(this.title);
        entity.setContent(this.content);
        return entity;
    }
}
