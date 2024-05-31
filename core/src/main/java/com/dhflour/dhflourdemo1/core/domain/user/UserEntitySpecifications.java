package com.dhflour.dhflourdemo1.core.domain.user;

import org.springframework.data.jpa.domain.Specification;

public class UserEntitySpecifications {

    public static Specification<UserEntity> usernameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + keyword + "%");
    }
}
