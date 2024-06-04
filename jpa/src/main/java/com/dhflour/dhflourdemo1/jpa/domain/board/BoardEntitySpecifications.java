package com.dhflour.dhflourdemo1.jpa.domain.board;

import org.springframework.data.jpa.domain.Specification;

public class BoardEntitySpecifications {

    public static Specification<BoardEntity> titleContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

}
