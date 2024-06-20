package com.dhflour.dhflourdemo1.api.service.category;

import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryAPIService {
    Flux<RBoardCategory> list(Sort sort);
    Mono<RBoardCategory> getById(Long id);
    Mono<RBoardCategory> create(RBoardCategory entity);
    Mono<RBoardCategory> update(Long id, RBoardCategory entity);
    Mono<Void> delete(Long id);
}
