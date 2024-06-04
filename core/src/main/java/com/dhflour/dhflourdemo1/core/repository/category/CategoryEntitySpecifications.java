package com.dhflour.dhflourdemo1.core.repository.category;

import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class CategoryEntitySpecifications {
    public static Specification<CategoryEntity> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
}
