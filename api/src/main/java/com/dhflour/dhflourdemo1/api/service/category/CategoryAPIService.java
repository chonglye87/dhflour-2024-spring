package com.dhflour.dhflourdemo1.api.service.category;

import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 카테고리 API 서비스의 주요 기능을 정의한다.
 * 목록 조회, 조회, 생성, 수정, 삭제 기능을 포함한다.
 */
public interface CategoryAPIService {
    /**
     * 정렬 기준에 따라 카테고리 목록을 반환한다.
     *
     * @param sort 정렬 정보
     * @return 카테고리 목록을 포함하는 Flux<RBoardCategory>
     */
    Flux<RBoardCategory> list(Sort sort);

    /**
     * 주어진 ID에 해당하는 카테고리 항목을 조회한다.
     *
     * @param id 조회할 카테고리 항목의 ID
     * @return 조회된 카테고리 항목을 포함하는 Mono<RBoardCategory>
     */
    Mono<RBoardCategory> getById(Long id);

    /**
     * 새로운 카테고리 항목을 생성한다.
     *
     * @param entity 생성할 카테고리 항목
     * @return 생성된 카테고리 항목을 포함하는 Mono<RBoardCategory>
     */
    Mono<RBoardCategory> create(RBoardCategory entity);

    /**
     * 주어진 ID에 해당하는 카테고리 항목을 수정한다.
     *
     * @param id 수정할 카테고리 항목의 ID
     * @param entity 수정할 카테고리 항목의 정보
     * @return 수정된 카테고리 항목을 포함하는 Mono<RBoardCategory>
     */
    Mono<RBoardCategory> update(Long id, RBoardCategory entity);

    /**
     * 주어진 ID에 해당하는 카테고리 항목을 삭제한다.
     *
     * @param id 삭제할 카테고리 항목의 ID
     * @return 삭제 작업의 완료를 나타내는 Mono<Void>
     */
    Mono<Void> delete(Long id);
}