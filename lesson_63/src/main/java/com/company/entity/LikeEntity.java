package com.company.entity;

import com.company.enums.LikeStatus;
import com.company.enums.LikeType;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "likes")
public class LikeEntity extends BaseEntity {

    @Column(nullable = false)
    private Integer actionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId")
    private ProfileEntity profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "likeStatus")
    private LikeStatus likeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "likeType")
    private LikeType type;


}
