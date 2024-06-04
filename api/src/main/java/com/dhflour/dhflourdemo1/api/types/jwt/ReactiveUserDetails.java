package com.dhflour.dhflourdemo1.api.types.jwt;

import com.dhflour.dhflourdemo1.api.domain.user.ReactiveUser;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Slf4j
@Getter
@ToString
public class ReactiveUserDetails extends User {

    private Long id;
    private String email;
    private String username;


    public ReactiveUserDetails(ReactiveUser user,
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

    public MyUserDetails toMyUserDetails() {
        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setId(this.id);
        myUserDetails.setUsername(this.username);
        myUserDetails.setEmail(this.email);
        return myUserDetails;
    }
}
