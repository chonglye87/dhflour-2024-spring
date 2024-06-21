package com.dhflour.dhflourdemo1.api.service.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import com.dhflour.dhflourdemo1.api.domain.board.RBoardRepository;
import com.dhflour.dhflourdemo1.api.utils.ModelMapperUtils;
import com.dhflour.dhflourdemo1.core.types.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class BoardAPIServiceImpl implements BoardAPIService {

    @Autowired
    private RBoardRepository repository;

    @Autowired
    private ModelMapperUtils modelMapperUtils;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<Page<RBoard>> page(Pageable pageable) {
        return this.repository.findAllBy(pageable)
                .collectList()
                .zipWith(this.repository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }


    @Override
    @Transactional(readOnly = true)
    public Mono<RBoard> getById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<RBoard> create(RBoard entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Mono<RBoard> update(Long id, RBoard entity) {
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
