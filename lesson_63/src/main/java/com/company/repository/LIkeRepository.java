package com.company.repository;

import com.company.dto.LikeCountDto;
import com.company.entity.ArticleEntity;
import com.company.entity.LikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.enums.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LIkeRepository extends JpaRepository<LikeEntity,Integer> {

    @Query("select count(l) from LikeEntity l where l.actionId=?1 and l.likeStatus=?2 and l.type=?3")
    Integer getLikeCount(Integer actionId, LikeStatus status, LikeType likeType);

    @Query("select l from LikeEntity l , ArticleEntity a where l.actionId=a.id and l.type=?1 and l.profile.id=?2")
    List<?> getlist(LikeType type,Integer profileId);

    Optional<LikeEntity> findByActionIdAndProfileAndType(Integer actionId, ProfileEntity profile,
                                                               LikeType likeType);

}
