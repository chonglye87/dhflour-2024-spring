package com.dhflour.dhflourdemo1.api.domain.board;

import com.dhflour.dhflourdemo1.api.domain.AbstractTable;
import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Setter
@Getter
@ToString
@Table("board")
public class RBoard extends AbstractTable<Long> {

    @Id
    private Long id;

    @Schema(description = "제목", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String title;

    @Schema(description = "내용", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String content;

    @Transient
    private List<RBoardCategory> categories;
}
