package com.company.dto.profile;

import com.company.controller.ProfileController;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotNull(message = "name can not be null")
    @NotEmpty(message = "name is Empty")
    @Size(min = 5,max = 15,message = "name length error")
    private String name;
    @NotNull(message = "surname can not be null")
    @NotEmpty(message = "surname is Empty")
    @Size(min = 5,max = 15,message = "surname length error")
    private String surname;
    @NotEmpty(message = "login is empty")
    @Size(min = 6, max = 15, message = "Login Length Error")
    private String login;
    @NotEmpty(message = "password is empty")
    @Size(min = 6, message = "Password Length Error")
    private String pswd;
    @Email
    private String email;

    private ProfileRole role;
    private ProfileStatus status;

    private String jwt; // token
}
