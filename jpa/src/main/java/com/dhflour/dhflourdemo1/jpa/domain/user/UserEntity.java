package com.dhflour.dhflourdemo1.jpa.domain.user;

import com.dhflour.dhflourdemo1.jpa.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User") // JPA
@Schema(description = "사용자 엔티티")
public class UserEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1", readOnly = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Column(nullable = false, length = 20)
    @Schema(description = "이름", example = "홍사남", readOnly = true)
    private String username;

    @Column(nullable = false, length = 12)
    @Schema(description = "휴대폰번호", example = "01011112222", readOnly = true)
    private String mobile;

    @Column(nullable = false, length = 255, unique = true)
    @Schema(description = "이메일", example = "chonglye@aartkorea.com", readOnly = true)
    private String email;

    @JsonIgnore
    @Column(length = 255)
    private String password; // 비밀번호

    @JsonIgnore
    @Transient
    private String passwordConfirm; // 비밀번호 확인

    @Embedded
    private UserAgreementEmbedded userAgreement;

    @Override
    public void delete() {

    }

    @Override
    public void lazy() {

    }
}
