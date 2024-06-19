package com.dhflour.dhflourdemo1.api.domain.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Table("category")
public class RBoardCategory {

    @Id
    private Long id;

    @Schema(description = "카테고리명", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "등록시간", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @CreatedDate
    private LocalDateTime createdAt;

    @Schema(description = "수정시간", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
