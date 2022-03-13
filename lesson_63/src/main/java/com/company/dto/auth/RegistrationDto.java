package com.company.dto.auth;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationDto {
    @NotEmpty(message = "Kalla name qani")
    private String name;
    @Size(min = 5, max = 15, message = "Surname xato")
    private String surname;

    @NotEmpty(message = "login is empty")
    @Size(min = 4, max = 15, message = "Login Length Error")
    private String login;
    @NotEmpty(message = "message is empty")
    @Size(min = 4, message = "Password Length Error")
    private String password;
    @Email(message = "Email Error")
    private String email;

}
