package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserRepository;
import com.dhflour.dhflourdemo1.api.domain.user.RequestRUser;
import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import com.dhflour.dhflourdemo1.api.utils.ModelMapperUtils;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private RUserRepository repository;

    @Autowired
    private ModelMapperUtils modelMapperUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<RUser>> page(PageFilter pageFilter) {
        return repository.findAllByFilter(pageFilter)
                .collectList()
                .zipWith(this.repository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageFilter.getPageable(), p.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RUser> getById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<RUser> create(RUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    @Transactional
    public Mono<RUser> update(Long id, RUser entity) {
        return repository.findById(id)
                .flatMap(existingEntity -> {
                    modelMapperUtils.mapSkippingFields(entity, existingEntity, "id", "password");
                    log.debug("Updating entity with existingEntity {}", existingEntity);
                    return repository.save(existingEntity);
                }).switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<RUser> updatePassword(Long id, String oldPassword, String newPassword) {
        return repository.findById(id)
                .flatMap(existingEntity -> {
                    if (!passwordEncoder.matches(oldPassword, existingEntity.getPassword())) {
                        return Mono.error(new BadRequestException("비밀번호가 일치하지 않습니다."));
                    }
                    existingEntity.setPassword(passwordEncoder.encode(newPassword));
                    return repository.save(existingEntity);
                }).switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(rBoardCategory -> repository.deleteById(rBoardCategory.getId()));
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll(List<Long> ids) {
        // 주어진 ID 목록에 해당하는 RUser 엔티티들을 조회
        return Flux.fromIterable(ids)
                .flatMap(id -> repository.findById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException())) // 만약 엔티티가 존재하지 않으면 NotFoundException 발생
                        .flatMap(existingEntity -> repository.deleteById(existingEntity.getId()))
                )
                .then(); // 모든 삭제 작업이 완료되면 Mono<Void> 반환
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> checkUnique(RequestRUser requestRUser, Long id) {
        return repository.findOneByEmail(requestRUser.getEmail())
                .map(existingUser -> existingUser.getId().equals(id))
                .log()
                .defaultIfEmpty(true);
    }
}
