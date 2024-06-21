package com.dhflour.dhflourdemo1.api.domain.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RUserRepository extends ReactiveCrudRepository<RUser, Long> {

    Mono<Boolean> existsByEmail(String email);
    Mono<RUser> findOneByEmail(String email);

    Flux<RUser> findAllBy(Pageable pageable);
}
