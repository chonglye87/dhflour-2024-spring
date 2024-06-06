package com.dhflour.dhflourdemo1.core.types.jwt;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyUserDetails {

    private Long id;
    private String email;
    private String username;
}
