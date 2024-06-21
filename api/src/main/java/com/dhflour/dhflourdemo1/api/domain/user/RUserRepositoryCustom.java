package com.dhflour.dhflourdemo1.api.domain.user;

import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import reactor.core.publisher.Flux;

public interface RUserRepositoryCustom {
    Flux<RUser> findAllByFilter(PageFilter pageFilter);
}
