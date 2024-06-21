package com.dhflour.dhflourdemo1.api.domain.board;

import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RBoardRepositoryCustom {
    Flux<RBoard> findAllByFilter(PageFilter pageFilter, List<Long> categoryIds);
}
