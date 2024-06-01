package com.dhflour.dhflourdemo1.core.types.user;

import com.dhflour.dhflourdemo1.core.domain.user.UserAgreementEmbedded;
import com.dhflour.dhflourdemo1.core.domain.user.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    @Schema(description = "이름", required = true, example = "홀길동")
    private String username;

    @Schema(description = "이메일", required = true, example = "test2@test.com")
    private String email;

    @Schema(description = "비밀번호", required = true, example = "1234")
    private String password;

    @Schema(description = "휴대폰번호", required = true, example = "01011112222")
    private String mobile;

    @Schema(description = "약관", required = true)
    private UserAgreementEmbedded agreement;

    public UserEntity toEntity(PasswordEncoder passwordEncoder) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPassword(this.password);
        userEntity.setUserAgreement(this.agreement);
        userEntity.setMobile(this.mobile);
        userEntity.setUserAgreement(this.agreement);
        return userEntity;
    }
}
