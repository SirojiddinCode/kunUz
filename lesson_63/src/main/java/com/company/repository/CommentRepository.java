package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> ,
        JpaSpecificationExecutor<CommentEntity> {
    @Transactional
    @Modifying
    @Query("update CommentEntity c set c.content=:content where c.id=:id and c.profile.id=:pid")
    void update(@Param("content")String content,@Param("id")Integer id,@Param("pid")Integer ownerId);
    boolean existsByIdAndProfile(Integer id, ProfileEntity profile);
    @Modifying
    @Transactional
    @Query("delete from CommentEntity  c where c.id=?1 and c.profile.id=?2")
    void deleteByIdAndProfile(Integer commentId, Integer profileId);

    List<CommentEntity> findAllByProfile(ProfileEntity profile);

    Page<CommentEntity> findAllByArticle_Id(Integer articleId, Pageable pageable);
}
