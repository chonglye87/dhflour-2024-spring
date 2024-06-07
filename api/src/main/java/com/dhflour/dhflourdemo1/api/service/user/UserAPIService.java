package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import reactor.core.publisher.Mono;

public interface UserAPIService {
    // 활성 유저
    Mono<RUser> getActiveUser(String email);
    // 활성 유저
    Mono<RUser> getActiveUser(Long id);
}
