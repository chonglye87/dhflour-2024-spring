package com.dhflour.dhflourdemo1.api.domain.board;

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
@Table("board")
public class RBoard extends AbstractTable<Long> {

    @Id
    private Long id;

    @Schema(description = "버전", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String title;
    private String content;
}
