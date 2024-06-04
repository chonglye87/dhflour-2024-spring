package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import reactor.core.publisher.Mono;

public interface UserAPIService {
    Mono<RUser> getUser(String email);
}
