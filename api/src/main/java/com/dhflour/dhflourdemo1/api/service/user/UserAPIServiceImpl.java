package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserAPIServiceImpl implements UserAPIService {

    @Autowired
    private RUserRepository userRepository;

    @Override
    @Transactional
    public Mono<RUser> getUser(String email) {
        return userRepository.findOneByEmail(email);
    }
}
