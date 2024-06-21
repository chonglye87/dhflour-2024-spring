package com.dhflour.dhflourdemo1.api.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestRUser {

    @Schema(description = "이메일", requiredMode = Schema.RequiredMode.REQUIRED, example = "test@gmail.com", maxLength = 255)
    private String email;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동", maxLength = 255)
    private String username;

    @Schema(description = "휴대폰번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "01011112222", maxLength = 255)
    private String mobile;

    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yuio1234!", maxLength = 255)
    private String password;

    @Schema(description = "이용약관동의 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean policy;

    @Schema(description = "개인정보처리방침동의 여부", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean privacy;

    @Schema(description = "마케팅수신동의 여부", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
    private boolean marketing;

    public RUser toEntity() {
        RUser entity = new RUser();
        entity.setEmail(this.email);
        entity.setUsername(this.username);
        entity.setMobile(this.mobile);
        entity.setPassword(this.password);
        entity.setPolicy(this.policy);
        entity.setPrivacy(this.privacy);
        entity.setMarketing(this.marketing);
        return entity;
    }
}
