package com.dhflour.dhflourdemo1.api.domain.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RUserRepository extends ReactiveCrudRepository<RUser, Long> {

    Mono<RUser> findOneByEmail(String email);
}
