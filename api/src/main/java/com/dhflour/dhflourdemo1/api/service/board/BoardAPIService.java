package com.dhflour.dhflourdemo1.api.service.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 게시판 API 서비스의 주요 기능을 정의한다.
 * 페이지네이션, 조회, 생성, 수정, 삭제 기능을 포함한다.
 */
public interface BoardAPIService {
    /**
     * 게시판 목록을 페이지네이션하여 반환한다.
     *
     * @param pageFilter 페이지네이션 정보
     * @return 게시판 목록을 포함하는 Mono<Page<RBoard>>
     */
    Mono<Page<RBoard>> page(PageFilter pageFilter, List<Long> categoryIds);

    /**
     * 주어진 ID에 해당하는 게시판 항목을 조회한다.
     *
     * @param id 조회할 게시판 항목의 ID
     * @return 조회된 게시판 항목을 포함하는 Mono<RBoard>
     */
    Mono<RBoard> getById(Long id);

    /**
     * 새로운 게시판 항목을 생성한다.
     *
     * @param entity 생성할 게시판 항목
     * @return 생성된 게시판 항목을 포함하는 Mono<RBoard>
     */
    Mono<RBoard> create(RBoard entity);
    Mono<RBoard> create(RBoard entity, List<Long> categoryIds);

    /**
     * 주어진 ID에 해당하는 게시판 항목을 수정한다.
     *
     * @param id 수정할 게시판 항목의 ID
     * @param entity 수정할 게시판 항목의 정보
     * @return 수정된 게시판 항목을 포함하는 Mono<RBoard>
     */
    Mono<RBoard> update(Long id, RBoard entity);
    Mono<RBoard> update(Long id, RBoard entity, List<Long> categoryIds);

    /**
     * 주어진 ID에 해당하는 게시판 항목을 삭제한다.
     *
     * @param id 삭제할 게시판 항목의 ID
     * @return 삭제 작업의 완료를 나타내는 Mono<Void>
     */
    Mono<Void> delete(Long id);
    Mono<Void> deleteAll(List<Long> ids);
}
