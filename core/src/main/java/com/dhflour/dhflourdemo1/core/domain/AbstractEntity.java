package com.dhflour.dhflourdemo1.core.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // 모든 필드에 json 적용
@JsonInclude(value = JsonInclude.Include.ALWAYS) // 모든 데이터에 json 적용
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<K extends java.io.Serializable> implements java.io.Serializable {

    private static final long serialVersionUID = 4729916107584169127L;

    // 생성한 날짜
    @Getter
    @Column(insertable = true, updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    // 생성한 사용자 아이디
    @JsonIgnore
    @Column(insertable = true, updatable = false)
    @CreatedBy
    protected Long createdBy;

    // 업데이트한 날짜
    @JsonIgnore
    @Getter
    @Column(insertable = true, updatable = true)
    @LastModifiedDate
    protected LocalDateTime updatedAt;

    // 업데이트한 사용자 아이디
    @JsonIgnore
    @Column(insertable = true, updatable = true)
    @LastModifiedBy
    protected Long updatedBy;

    // 버전관리 트렉젝션이 동작할때마다 버전이 +1이 업데이트 된다. ORM책 p.689 참고
    @Getter
    @Version
    @Column(columnDefinition = "BIGINT(20) default 0")
    @JsonIgnore
    protected Long version;

    public abstract String toString();

    public abstract K getId();

    // Data 삭제시 처리 함수
    public abstract void delete();

    // 영속성 Lazy Loading 이 필요한 기본값
    public abstract void lazy();

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (getId() == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) obj;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }
}
