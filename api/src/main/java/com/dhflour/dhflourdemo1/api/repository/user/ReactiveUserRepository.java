package com.dhflour.dhflourdemo1.api.repository.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends ReactiveCrudRepository<ReactiveUser, Long> {

    Mono<ReactiveUser> findOneByEmail(String email);
}
