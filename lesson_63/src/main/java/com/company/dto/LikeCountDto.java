package com.company.dto;

import com.company.enums.LikeType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeCountDto {

    private Integer actionId;
    private Integer likeCount;
    private Integer disLikeCount;
    private LikeType likeType;

}
