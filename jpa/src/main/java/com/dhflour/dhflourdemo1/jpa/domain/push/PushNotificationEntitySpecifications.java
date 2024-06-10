package com.dhflour.dhflourdemo1.jpa.domain.push;

import com.dhflour.dhflourdemo1.jpa.domain.board.BoardEntity;
import org.springframework.data.jpa.domain.Specification;

public class PushNotificationEntitySpecifications {

    public static Specification<BoardEntity> messageContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("message"), "%" + keyword + "%");
    }

}
