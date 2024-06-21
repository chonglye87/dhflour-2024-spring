package com.dhflour.dhflourdemo1.api.domain.category;

import com.dhflour.dhflourdemo1.api.domain.AbstractTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("category")
public class RBoardCategory extends AbstractTable<Long> {

    @Id
    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "카테고리명", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;
}
