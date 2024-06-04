package com.dhflour.dhflourdemo1.core.types.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class MyUserDetails {

    private Long id;
    private String email;
    private String username;
}
