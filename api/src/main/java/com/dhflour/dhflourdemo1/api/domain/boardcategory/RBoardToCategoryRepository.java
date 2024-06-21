package com.dhflour.dhflourdemo1.api.domain.boardcategory;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RBoardToCategoryRepository extends ReactiveCrudRepository<RBoardToCategory, Long> {
    Flux<RBoardToCategory> findByBoardId(Long boardId);
    Mono<Void> deleteByBoardId(Long boardId);
}