package com.dhflour.dhflourdemo1.api.service.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface BoardAPIService {
    Mono<Page<RBoard>> page(Pageable pageable);
    Mono<RBoard> getById(Long id);
    Mono<RBoard> create(RBoard entity);
    Mono<RBoard> update(Long id, RBoard entity);
    Mono<Void> delete(Long id);
}
