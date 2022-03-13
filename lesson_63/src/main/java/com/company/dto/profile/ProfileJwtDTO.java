package com.company.dto.profile;

import com.company.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileJwtDTO {
    private Integer id;
    private ProfileRole role;
}
