package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserRepository;
import com.dhflour.dhflourdemo1.api.domain.user.RequestRUser;
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
import reactor.core.publisher.Mono;

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
    public Mono<Page<RUser>> page(Pageable pageable) {
        log.debug("pageable : {}", pageable);
        return repository.findAllBy(pageable)
                .collectList()
                .zipWith(this.repository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
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
    @Transactional(readOnly = true)
    public Mono<Boolean> checkUnique(RequestRUser requestRUser, Long id) {
        return repository.findOneByEmail(requestRUser.getEmail())
                .map(existingUser -> existingUser.getId().equals(id))
                .log()
                .defaultIfEmpty(true);
    }
}
