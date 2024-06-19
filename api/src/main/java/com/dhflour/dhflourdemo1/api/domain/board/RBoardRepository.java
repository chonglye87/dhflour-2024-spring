package com.dhflour.dhflourdemo1.api.domain.board;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RBoardRepository extends ReactiveCrudRepository<RBoard, Long> {
}
