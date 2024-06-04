package com.dhflour.dhflourdemo1.core.reactive.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends ReactiveCrudRepository<ReactiveUser, Long> {

    Mono<ReactiveUser> findOneByEmail(String email);
}
