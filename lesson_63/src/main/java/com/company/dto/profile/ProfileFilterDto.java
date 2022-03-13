package com.company.dto.profile;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProfileFilterDto {
    private String name;
    private String surname;
    private String email;
    private ProfileRole role;
    private ProfileStatus status;
    private Integer id;
    private LocalDate fromDate;
    private LocalDate toDate;
}
