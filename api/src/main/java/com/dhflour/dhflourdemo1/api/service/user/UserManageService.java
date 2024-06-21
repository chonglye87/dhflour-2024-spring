package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestRUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface UserManageService {
    Mono<Page<RUser>> page(Pageable pageable);
    Mono<RUser> getById(Long id);
    Mono<RUser> create(RUser entity);
    Mono<RUser> update(Long id, RUser entity);
    Mono<RUser> updatePassword(Long id, String oldPassword, String newPassword);
    Mono<Void> delete(Long id);

    Mono<Boolean> checkUnique(RequestRUser requestBody, Long id);
}
