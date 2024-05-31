package com.dhflour.dhflourdemo1.core.service.userdetail;

import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.core.domain.user.UserEntityRepository;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.debug("Loading user by email {}", email);
        return userRepository.findOneByEmail(email).map(user -> {

                    boolean enabled = true;
                    boolean accountNonExpired = true;
                    boolean credentialsNonExpired = true;
                    boolean accountNonLocked = true;
                    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                    return new MyUserDetails(
                            user,
                            enabled,
                            accountNonExpired,
                            credentialsNonExpired,
                            accountNonLocked,
                            authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}