package com.dhflour.dhflourdemo1.core.repository.board;

import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import org.springframework.data.jpa.domain.Specification;

public class BoardEntitySpecifications {

    public static Specification<BoardEntity> titleContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

}
