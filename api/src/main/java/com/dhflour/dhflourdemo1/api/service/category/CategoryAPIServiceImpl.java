package com.dhflour.dhflourdemo1.api.service.category;

import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategoryRepository;
import com.dhflour.dhflourdemo1.api.utils.ModelMapperUtils;
import com.dhflour.dhflourdemo1.core.types.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CategoryAPIServiceImpl implements CategoryAPIService {

    @Autowired
    private RBoardCategoryRepository repository;

    @Autowired
    private ModelMapperUtils modelMapperUtils;

    @Override
    @Transactional(readOnly = true)
    public Flux<RBoardCategory> list(Sort sort) {
        return repository.findAll(sort)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RBoardCategory> getById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<RBoardCategory> create(RBoardCategory entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Mono<RBoardCategory> update(Long id, RBoardCategory entity) {
        return repository.findById(id)
                .flatMap(existingEntity -> {
                    modelMapperUtils.mapSkippingFields(entity, existingEntity, "id");
                    return repository.save(existingEntity);
                }).switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(rBoardCategory -> repository.deleteById(rBoardCategory.getId()));
    }
}
