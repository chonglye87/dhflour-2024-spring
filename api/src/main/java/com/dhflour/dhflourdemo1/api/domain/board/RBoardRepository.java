package com.dhflour.dhflourdemo1.api.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * 게시판 엔티티에 대한 CRUD 작업을 지원하는 리포지토리이다.
 * ReactiveCrudRepository를 확장하여 반응형 방식으로 데이터베이스 작업을 수행한다.
 */
@Repository
public interface RBoardRepository extends ReactiveCrudRepository<RBoard, Long>, RBoardRepositoryCustom {

    /**
     * 페이지네이션 정보에 따라 모든 게시판 항목을 조회한다.
     *
     * @param pageable 페이지네이션 정보
     * @return 게시판 항목을 포함하는 Flux<RBoard>
     */
    Flux<RBoard> findAllBy(Pageable pageable);
}
