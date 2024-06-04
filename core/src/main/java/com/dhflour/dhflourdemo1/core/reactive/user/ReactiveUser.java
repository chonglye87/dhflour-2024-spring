package com.dhflour.dhflourdemo1.core.reactive.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Table("User")
public class ReactiveUser {

    @Id
    private Long id;
    private String username;
    private String mobile;
    private String email;
    private String password;
    private boolean policy;
    private boolean privacy;
    private boolean marketing;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private Long createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private Long updateBy;

    @Version
    private Long version;
}