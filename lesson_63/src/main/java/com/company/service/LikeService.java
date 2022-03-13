package com.company.service;

import com.company.dto.LikeCountDto;
import com.company.dto.LikeDto;
import com.company.entity.LikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.enums.LikeType;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.LIkeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LIkeRepository lIkeRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;


    public LikeDto create(LikeDto likeDTO, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        Optional<LikeEntity> likeOptional = lIkeRepository.findByActionIdAndProfileAndType(likeDTO.getActionId(),
                profileEntity, likeDTO.getType());

        if (likeOptional.isPresent()) {
            LikeEntity lEntity = likeOptional.get();
            lEntity.setLikeStatus(likeDTO.getLikeStatus());
            lIkeRepository.save(lEntity);
            likeDTO.setId(lEntity.getId());
            return likeDTO;
        }

        LikeEntity entity = new LikeEntity();
        entity.setProfile(profileEntity);
        entity.setLikeStatus(likeDTO.getLikeStatus());
        entity.setType(likeDTO.getType());
        entity.setActionId(likeDTO.getActionId());

        lIkeRepository.save(entity);
        likeDTO.setId(entity.getId());
        return likeDTO;
    }

    public void delete(Integer likeId, Integer profileId) {
        Optional<LikeEntity> optional = lIkeRepository.findById(likeId);
        if (optional.isPresent()) {
            LikeEntity likeEntity = optional.get();
            if (likeEntity.getProfile().getId().equals(profileId)) {
                lIkeRepository.deleteById(likeId);
            }else
            throw new BadRequestException("This user is not allowed");
        }else
        throw new ItemNotFoundException("Like Not Found");
    }

    public void update(Integer profileId, LikeDto dto) {
        LikeEntity entity = get(dto.getId());
        if (!entity.getProfile().getId().equals(profileId)) {
            throw new BadRequestException("Not Owner");
        }
        entity.setLikeStatus(dto.getLikeStatus());
        lIkeRepository.save(entity);
    }

    public LikeEntity get(Integer likeId) {
        Optional<LikeEntity> op = lIkeRepository.findById(likeId);
        return op.orElseThrow(() -> new ItemNotFoundException("Not Found"));
    }

    public LikeCountDto likeAndDislikeCount(Integer articleId,LikeType type) {
        if (articleService.existsById(articleId)) {
            Integer likeCount = lIkeRepository.getLikeCount(articleId, LikeStatus.LIKE, type);
            Integer disLikeCount=lIkeRepository.getLikeCount(articleId,LikeStatus.DIS_LIKE,type);
            return new LikeCountDto(articleId,likeCount,disLikeCount,type);
        }
        throw new BadRequestException("article Not Found");
    }

    /*public List<ArticleDTO> likedArticleList(Integer profileId){
        lIkeRepository.getlist(LikeType.Article,profileId);
    }*/

}
