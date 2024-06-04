package com.dhflour.dhflourdemo1.api.domain.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RUserRepository extends ReactiveCrudRepository<RUser, Long> {

    Mono<RUser> findOneByEmail(String email);
}
