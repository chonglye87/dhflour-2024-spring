package com.dhflour.dhflourdemo1.api.domain.boardcategory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@Table("board_category")
public class RBoardToCategory {

    private Long boardId;
    private Long categoryId;
}
