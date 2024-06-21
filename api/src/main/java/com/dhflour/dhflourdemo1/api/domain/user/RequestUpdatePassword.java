package com.dhflour.dhflourdemo1.api.domain.user;

import lombok.Data;

@Data
public class RequestUpdatePassword {
    private String oldPassword;
    private String newPassword;
}
