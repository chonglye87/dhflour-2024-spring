package com.dhflour.dhflourdemo1.api.service.user;

import com.dhflour.dhflourdemo1.api.domain.user.RUser;
import com.dhflour.dhflourdemo1.api.domain.user.RUserRepository;
import com.dhflour.dhflourdemo1.api.domain.user.RequestSignUp;
import com.dhflour.dhflourdemo1.api.types.jwt.AuthenticationResponse;
import com.dhflour.dhflourdemo1.api.types.jwt.ReactiveUserDetails;
import com.dhflour.dhflourdemo1.api.web.auth.AuthController;
import com.dhflour.dhflourdemo1.core.service.jwt.JWTSymmetricService;
import com.dhflour.dhflourdemo1.core.types.jwt.MyUserDetails;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.dhflour.dhflourdemo1.core.utils.WebUtils.getClientIp;
import static com.dhflour.dhflourdemo1.core.utils.WebUtils.getDeviceDetector;

@Slf4j
@Service
public class UserAPIServiceImpl implements UserAPIService {

    @Autowired
    private RUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTSymmetricService jwtService;

    @Override
    @Observed(name = "user.name",
            contextualName = "getting-user-name",
            lowCardinalityKeyValues = {"userType", "userType2"})
    @Transactional(readOnly = true)
    public Mono<RUser> getActiveUser(String email) {
        return userRepository.findOneByEmail(email); // TODO 활성 여부 체크 (탈퇴 및 잠금)
    }

    @Observed(name = "user.name",
            contextualName = "getting-user-name",
            lowCardinalityKeyValues = {"userType", "userType2"})
    @Override
    @Transactional(readOnly = true)
    public Mono<RUser> getActiveUser(Long id) {
        return userRepository.findById(id); // TODO 활성 여부 체크 (탈퇴 및 잠금)
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AuthenticationResponse> authenticate(Authentication authentication, ServerWebExchange exchange) {
        try {
            final ReactiveUserDetails userDetails = (ReactiveUserDetails) authentication.getPrincipal();
            final MyUserDetails myUserDetails = userDetails.toMyUserDetails();
            final String accessToken = jwtService.generateToken(myUserDetails);
            final String refreshToken = jwtService.generateRefreshToken(myUserDetails);
            this.saveRefreshToken(refreshToken, exchange); // TODO 저장토큰 비교 및 검증 & 문제 없으면 기존 토큰 삭제
            return this.getActiveUser(myUserDetails.getEmail())
                    .map(user -> new AuthenticationResponse(accessToken, refreshToken, user))
                    .switchIfEmpty(Mono.error(AuthController.ERROR_USER_NOT_FOUND));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.error(AuthController.ERROR_USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public Mono<RUser> signUp(RequestSignUp body) {
        RUser entity = body.toEntity();
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> exist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void saveRefreshToken(String refreshToken, ServerWebExchange exchange) {
        // TODO Refresh Token 의 유효성 체크
        final String clientVersion = exchange.getRequest().getHeaders().getFirst("X-Client-Version");
        log.info("exchange.getRequest().getHeaders(): {}", exchange.getRequest().getHeaders());
        log.info("clientVersion: {}", clientVersion);
        log.info("ClientIp: {}", getClientIp(exchange));
        getDeviceDetector(exchange);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> varifyRefreshToken(String refreshToken, ServerWebExchange exchange) {
        userRepository.findById(1L);
        // TODO Refresh Token 의 유효성 체크
        final String clientVersion = exchange.getRequest().getHeaders().getFirst("X-Client-Version");
        log.info("clientVersion: {}", clientVersion);
        getDeviceDetector(exchange);
        return Mono.just(true);
    }
}
