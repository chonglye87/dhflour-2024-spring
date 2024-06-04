package com.dhflour.dhflourdemo1.api.service.userdetail;

import com.dhflour.dhflourdemo1.core.reactive.user.ReactiveUserRepository;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;
    
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return reactiveUserRepository.findOneByEmail(username).map(user -> {

                    boolean enabled = true;
                    boolean accountNonExpired = true;
                    boolean credentialsNonExpired = true;
                    boolean accountNonLocked = true;
                    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                    MyUserDetails userDetails =  new MyUserDetails(
                            user,
                            enabled,
                            accountNonExpired,
                            credentialsNonExpired,
                            accountNonLocked,
                            authorities);

                    return userDetails;
                });
    }
}
