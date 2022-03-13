package com.company.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class AuthorizationDTO {
    @NotEmpty(message = "login is empty")
    @Size(min = 4, max = 15, message = "Login Length Error")
    private String login;
    @NotEmpty(message = "Password is empty")
    @Size(min = 4, message = "Password Length Error")
    private String password;

}
