package com.dhflour.dhflourdemo1.core.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Embeddable
public class UserAgreementEmbedded implements Serializable {

    // 서비스 이용약관
    @Schema(description = "서비스 이용약관", example = "true", defaultValue = "false")
    private boolean policy;

    // 개인정보처리방침
    @Schema(description = "개인정보처리방침", example = "true", defaultValue = "false")
    private boolean privacy;

    // 마케팅 정보 수신 동의
    @Schema(description = "마케팅 정보 수신 동의", example = "true", defaultValue = "false")
    private boolean marketing;
}