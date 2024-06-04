package com.dhflour.dhflourdemo1.jpa.service.user;

import com.dhflour.dhflourdemo1.jpa.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.jpa.types.PageFilter;
import org.springframework.data.domain.Page;

import java.util.Locale;

public interface UserService {

    // Create
    UserEntity create(UserEntity entity);

    // Update
    UserEntity update(UserEntity entity);

    // Delete
    void delete(UserEntity entity);

    // get Object
    UserEntity get(Locale locale, Long id);

    // Pagination
    Page<UserEntity> page(PageFilter filter);
}
