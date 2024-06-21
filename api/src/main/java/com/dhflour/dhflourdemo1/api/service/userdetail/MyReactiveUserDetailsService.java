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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * MyReactiveUserDetailsService 클래스는 사용자 세부 정보를 조회하고,
 * UserDetails 객체를 반환하는 서비스를 구현한다.
 */
@Service
public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserAPIService userAPIService;

    /**
     * 주어진 RUser 객체로부터 UserDetails 객체를 생성한다.
     *
     * @param user 사용자 정보
     * @return 생성된 UserDetails 객체를 포함하는 Mono<UserDetails>
     */
    private Mono<UserDetails> getUserDetails(RUser user) {
        boolean enabled = true; // 계정 활성 상태
        boolean accountNonExpired = true; // 계정 만료 여부
        boolean credentialsNonExpired = true; // 자격 증명 만료 여부
        boolean accountNonLocked = true; // 계정 잠김 여부
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한 설정

        UserDetails userDetails = new ReactiveUserDetails(
                user,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);

        return Mono.just(userDetails); // UserDetails 객체를 Mono로 반환
    }

    /**
     * 주어진 사용자 이름(username)에 해당하는 사용자 세부 정보를 조회한다.
     *
     * @param username 사용자 이름 또는 ID
     * @return 조회된 UserDetails 객체를 포함하는 Mono<UserDetails>
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (NumberUtils.isCreatable(username)) { // 주어진 문자열이 숫자인지 확인
            return userAPIService.getActiveUser(Long.parseLong(username)) // 숫자일 경우 ID로 사용자 조회
                    .flatMap(this::getUserDetails); // 조회된 사용자 정보로 UserDetails 생성
        } else {
            return userAPIService.getActiveUser(username) // 숫자가 아닐 경우 이메일로 사용자 조회
                    .flatMap(this::getUserDetails); // 조회된 사용자 정보로 UserDetails 생성
        }
    }
}