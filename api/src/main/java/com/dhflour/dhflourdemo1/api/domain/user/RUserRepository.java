package com.dhflour.dhflourdemo1.api.domain.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * 사용자 엔티티에 대한 CRUD 작업을 지원하는 리포지토리이다.
 * ReactiveCrudRepository를 확장하여 반응형 방식으로 데이터베이스 작업을 수행한다.
 */
@Repository
public interface RUserRepository extends ReactiveCrudRepository<RUser, Long>, RUserRepositoryCustom {

    /**
     * 주어진 이메일이 존재하는지 확인한다.
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 여부를 포함하는 Mono<Boolean>
     */
    Mono<Boolean> existsByEmail(String email);

    /**
     * 주어진 이메일에 해당하는 사용자 정보를 조회한다.
     *
     * @param email 조회할 이메일
     * @return 조회된 사용자 정보를 포함하는 Mono<RUser>
     */
    Mono<RUser> findOneByEmail(String email);

    /**
     * 페이지네이션 정보에 따라 모든 사용자 정보를 조회한다.
     *
     * @param pageable 페이지네이션 정보
     * @return 사용자 정보를 포함하는 Flux<RUser>
     */
    Flux<RUser> findAllBy(Pageable pageable);
}
