package com.dhflour.dhflourdemo1.api.domain.category;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RBoardCategoryRepository extends ReactiveCrudRepository<RBoardCategory, Long> {
    Flux<RBoardCategory> findAll(Sort sort);
}
