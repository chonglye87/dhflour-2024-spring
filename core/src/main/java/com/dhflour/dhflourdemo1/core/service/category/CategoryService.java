package com.dhflour.dhflourdemo1.core.service.category;

import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

public interface CategoryService {
    // Create
    CategoryEntity create(CategoryEntity entity);

    // Update
    CategoryEntity update(CategoryEntity entity);

    // Delete
    void delete(CategoryEntity entity);

    // get Object
    CategoryEntity get(Locale locale, Long id);

    // Pagination
    Page<CategoryEntity> page(PageFilter filter);

    List<CategoryEntity> listByIds(List<Long> ids);
}
