package com.dhflour.dhflourdemo1.api.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RBoardRepository extends ReactiveCrudRepository<RBoard, Long> {
    Flux<RBoard> findAllBy(Pageable pageable);
}
