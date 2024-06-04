package com.dhflour.dhflourdemo1.api.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Table("user")
public class RUser {

    @Id
    private Long id;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
    private String username;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "01011112222")
    private String mobile;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "test@test.com")
    private String email;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    private String password;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean policy;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean privacy;

    @Schema(description = "사용자명", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
    private boolean marketing;

    @CreatedDate
    @Schema(description = "등록시간", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private LocalDateTime createdAt;

    @CreatedBy
    @Schema(description = "등록자", hidden = true)
    private Long createdBy;

    @LastModifiedDate
    @Schema(description = "수정시간", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Schema(description = "수정자", hidden = true)
    private Long updatedBy;

    @Version
    @Schema(description = "버전", hidden = true)
    private Long version;
}
