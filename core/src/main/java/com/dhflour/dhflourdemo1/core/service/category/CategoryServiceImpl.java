package com.dhflour.dhflourdemo1.core.service.category;

import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntityRepository;
import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntitySpecifications;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.UpdateErrorException;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final String ENTITY_NAME = CategoryEntity.class.getName();

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public CategoryEntity create(CategoryEntity entity) {
        // Validation 처리
        return categoryEntityRepository.save(entity);
    }

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public CategoryEntity update(CategoryEntity entity) {
        if (entity.getId() == null) {
            throw new BadRequestException();
        }
        // Validation 처리
        return categoryEntityRepository.findById(entity.getId())
                .map(original -> {
                    BeanUtils.copyProperties(entity, original, "id");
                    return categoryEntityRepository.save(original);
                }).orElseThrow(() -> new UpdateErrorException(entity.getId(), ENTITY_NAME));
    }

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public void delete(CategoryEntity entity) {
        categoryEntityRepository.findById(entity.getId())
                .ifPresent(CategoryEntity -> {
                    categoryEntityRepository.delete(CategoryEntity);
                });
    }

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랙젝션
    public CategoryEntity get(Locale locale, Long id) {
        return categoryEntityRepository.findById(id).map(categoryEntity -> {
            // locale 다국어 처리
            categoryEntity.getBoards().size();
            return categoryEntity;
        }).orElse(null);
    }

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랙젝션
    public Page<CategoryEntity> page(PageFilter filter) {
        Specification<CategoryEntity> spec = Specification.where(null);

        if (filter.getQuery() != null && !filter.getQuery().isEmpty()) {
            spec = spec.and(CategoryEntitySpecifications.nameContains(filter.getQuery()));
        }
        Page<CategoryEntity> page = categoryEntityRepository.findAll(spec, filter.getPageable());
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryEntity> listByIds(List<Long> ids) {
        return categoryEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
