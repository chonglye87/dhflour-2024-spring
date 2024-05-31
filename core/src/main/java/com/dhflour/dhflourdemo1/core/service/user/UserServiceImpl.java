package com.dhflour.dhflourdemo1.core.service.user;

import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.core.domain.user.UserEntityRepository;
import com.dhflour.dhflourdemo1.core.domain.user.UserEntitySpecifications;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.UpdateErrorException;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private final String ENTITY_NAME = UserEntity.class.getName();

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserEntity create(UserEntity entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userEntityRepository.save(entity);
    }

    @Override
    @Transactional
    public UserEntity update(UserEntity entity) {
        if (entity.getId() == null) {
            throw new BadRequestException();
        }
        return userEntityRepository.findById(entity.getId())
                .map(original -> {
                    BeanUtils.copyProperties(entity, original, "id");
                    return userEntityRepository.save(original);
                }).orElseThrow(() -> new UpdateErrorException(entity.getId(), ENTITY_NAME));
    }

    @Override
    @Transactional
    public void delete(UserEntity entity) {
        userEntityRepository.findById(entity.getId())
                .ifPresent(_entity -> {
                    userEntityRepository.delete(_entity);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity get(Locale locale, Long id) {
        return userEntityRepository.findById(id).map(entity -> {
            // 후처리
            return entity;
        }).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEntity> page(PageFilter filter) {
        Specification<UserEntity> spec = Specification.where(null);

        if (filter.getQuery() != null && !filter.getQuery().isEmpty()) {
            spec = spec.and(UserEntitySpecifications.usernameContains(filter.getQuery()));
        }
        Page<UserEntity> page = userEntityRepository.findAll(spec, filter.getPageable());
        return page;
    }
}
