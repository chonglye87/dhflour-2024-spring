package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RequestRUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;


/**
 * 사용자 관리 서비스의 주요 기능을 정의한다.
 * 페이지네이션, 조회, 생성, 수정, 비밀번호 변경, 삭제, 고유성 확인 기능을 포함한다.
 */
public interface UserManageService {
    /**
     * 사용자 목록을 페이지네이션하여 반환한다.
     *
     * @param pageable 페이지네이션 정보
     * @return 사용자 목록을 포함하는 Mono<Page<RUser>>
     */
    Mono<Page<RUser>> page(Pageable pageable);

    /**
     * 주어진 ID에 해당하는 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> getById(Long id);

    /**
     * 새로운 사용자를 생성한다.
     *
     * @param entity 생성할 사용자
     * @return 생성된 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> create(RUser entity);

    /**
     * 주어진 ID에 해당하는 사용자를 수정한다.
     *
     * @param id 수정할 사용자의 ID
     * @param entity 수정할 사용자의 정보
     * @return 수정된 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> update(Long id, RUser entity);

    /**
     * 주어진 ID에 해당하는 사용자의 비밀번호를 변경한다.
     *
     * @param id 사용자의 ID
     * @param oldPassword 현재 비밀번호
     * @param newPassword 새 비밀번호
     * @return 비밀번호가 변경된 사용자를 포함하는 Mono<RUser>
     */
    Mono<RUser> updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 주어진 ID에 해당하는 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 ID
     * @return 삭제 작업의 완료를 나타내는 Mono<Void>
     */
    Mono<Void> delete(Long id);

    /**
     * 주어진 사용자 정보가 고유한지 확인한다.
     *
     * @param requestBody 확인할 사용자 정보
     * @param id 사용자 ID
     * @return 고유성 여부를 포함하는 Mono<Boolean>
     */
    Mono<Boolean> checkUnique(RequestRUser requestBody, Long id);
}