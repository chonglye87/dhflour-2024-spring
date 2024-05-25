package com.dhflour.dhflourdemo1.core.types.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserSampleBody implements Serializable {
    private Long id;
    private String username;
}
