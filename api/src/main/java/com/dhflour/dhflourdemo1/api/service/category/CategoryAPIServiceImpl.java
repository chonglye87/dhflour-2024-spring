package com.dhflour.dhflourdemo1.api.service.category;

import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryAPIServiceImpl implements CategoryAPIService {

    @Autowired
    private RBoardCategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<RBoardCategory> list(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RBoardCategory> getById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<RBoardCategory> create(RBoardCategory category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Mono<RBoardCategory> update(Long id, RBoardCategory category) {
        return categoryRepository.findById(id)
                .flatMap(existingCategory -> {
                    BeanUtils.copyProperties(category, existingCategory, "id");
                    return categoryRepository.save(existingCategory);
                });
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return categoryRepository.deleteById(id);
    }
}
