package com.dhflour.dhflourdemo1.core.types.jwt;

import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import com.dhflour.dhflourdemo1.core.reactive.user.ReactiveUser;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Slf4j
@Getter
@ToString
public class MyUserDetails extends User {

    private Long id;
    private String email;
    private String username;


    public MyUserDetails(ReactiveUser user,
                         boolean enabled,
                         boolean accountNonExpired,
                         boolean credentialsNonExpired,
                         boolean accountNonLocked,
                         Set<GrantedAuthority> authorities) {

        super(user.getEmail(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
