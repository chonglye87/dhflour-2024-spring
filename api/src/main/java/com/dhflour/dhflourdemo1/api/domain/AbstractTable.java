package com.dhflour.dhflourdemo1.api.domain;

import com.dhflour.dhflourdemo1.core.utils.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

@Data
public abstract class AbstractTable<K extends java.io.Serializable> implements java.io.Serializable {
    private static final long serialVersionUID = 4729916107584169128L;

    @Schema(description = "등록시간", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Integer.class, example = "")
    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime createdAt;

    @Schema(description = "등록자", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @CreatedBy
    protected Long createdBy;

    @Schema(description = "수정시간", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Integer.class, example = "")
    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime updatedAt;

    @Schema(description = "수정자", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @LastModifiedBy
    protected Long updatedBy;

    @Schema(description = "버전", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @Version
    protected Long version;

    public abstract String toString();

    public abstract K getId();

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (getId() == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractTable that = (AbstractTable) obj;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

}
