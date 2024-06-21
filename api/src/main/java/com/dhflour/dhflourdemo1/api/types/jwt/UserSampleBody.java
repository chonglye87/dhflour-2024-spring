package com.dhflour.dhflourdemo1.api.types.jwt;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSampleBody implements Serializable {

    private Long id;
    private String username;
}
