package com.dhflour.dhflourdemo1.api.service.userdetail;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.service.user.UserAPIService;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserAPIService userAPIService;

    private Mono<UserDetails> getUserDetails(RUser user) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new ReactiveUserDetails(
                user,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);

        return Mono.just(userDetails);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (NumberUtils.isCreatable(username)) {
            return userAPIService.getActiveUser(Long.parseLong(username))
                    .flatMap(this::getUserDetails);
        } else {
            return userAPIService.getActiveUser(username)
                    .flatMap(this::getUserDetails);
        }

    }

}
