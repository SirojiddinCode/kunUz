package com.company.dto;

import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.enums.LikeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDto {

    private Integer id;

    private Integer actionId;

    private Integer profileId;

    private LocalDateTime createdDate;

    private LikeStatus likeStatus;

    private LikeType type;
}
