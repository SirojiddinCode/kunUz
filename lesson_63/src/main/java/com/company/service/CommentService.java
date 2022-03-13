package com.company.service;

import com.company.dto.comment.CommentDto;
import com.company.dto.comment.CommentFilterDto;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.repositoryImpl.CommentCustomRepositoryImpl;
import com.company.repository.CommentRepository;
import com.company.spec.ArticleSpecification;
import com.company.spec.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentCustomRepositoryImpl commentCustomRepository;

    public CommentDto create(CommentDto dto, Integer profileId) {
        ArticleEntity article = articleService.get(dto.getArticleId());
        ProfileEntity profile = profileService.get(profileId);
        if (dto.getContent().isEmpty() || dto.getContent() == null) {
            throw new BadRequestException("Content can not be null");
        }
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setProfile(profile);
        entity.setArticle(article);
        entity.setCreatedDate(LocalDateTime.now());
        commentRepository.save(entity);
        dto.setId(entity.getId());
        dto.setProfileId(profileId);
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public CommentEntity getById(Integer commentId) {
        Optional<CommentEntity> op = commentRepository.findById(commentId);
        return op.orElseThrow(() -> new ItemNotFoundException("Comment not found"));
    }

    public CommentDto get(Integer commentId) {
        return toDto(getById(commentId));
    }

    public CommentDto toDto(CommentEntity entity) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(entity.getId());
        commentDto.setContent(entity.getContent());
        commentDto.setProfileId(entity.getProfile().getId());
        commentDto.setArticleId(entity.getArticle().getId());
        commentDto.setCreatedDate(entity.getCreatedDate());
        return commentDto;
    }

    public void update(CommentDto dto, Integer ownerId, Integer commentId) {
        ProfileEntity profile = profileService.get(ownerId);
        if (commentRepository.existsByIdAndProfile(commentId, profile)) {
            if (dto.getContent() == null || dto.getContent().isEmpty()) {
                throw new BadRequestException("Content can not be null");
            }
            commentRepository.update(dto.getContent(), commentId, ownerId);
        }else
        throw new ItemNotFoundException("You cannot update this comment");
    }

    public void delete(Integer commentId, Integer profileId) {
        ProfileEntity profile = profileService.get(profileId);
        if (commentRepository.existsByIdAndProfile(commentId, profile)) {
            commentRepository.deleteByIdAndProfile(commentId, profileId);
        }else
        throw new ItemNotFoundException("Comment not Found");
    }

    public List<CommentDto> getAllByProfile(Integer profileId) {
        ProfileEntity profile = profileService.get(profileId);
        List<CommentEntity> list = commentRepository.findAllByProfile(profile);
        List<CommentDto> dtoList = new LinkedList<>();
        list.forEach(c -> dtoList.add(toDto(c)));
        return dtoList;
    }

    public PageImpl<CommentDto> getAllByArticle(Integer articleId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdDate");
        Page<CommentEntity> pages = commentRepository.findAllByArticle_Id(articleId, pageable);
        long totalElemnts = pages.getTotalElements();
        List<CommentEntity> entityList = pages.getContent();
        List<CommentDto> dtoList = new LinkedList<>();
        entityList.forEach(e -> dtoList.add(toDto(e)));
        return new PageImpl<>(dtoList, pageable, totalElemnts);
    }

    public boolean existsById(Integer commentId) {
       return commentRepository.existsById(commentId);
    }

   /* public PageImpl<CommentDto> filter(int page, int size, CommentFilterDto filterDto){
        PageImpl<CommentEntity> entityPage=commentCustomRepository.filter(page,size,filterDto);
        List<CommentDto> dtoList=entityPage.stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,entityPage.getPageable(),entityPage.getTotalElements());
    }*/

     public PageImpl<CommentDto> filter(int page, int size, CommentFilterDto filterDto){
         Pageable pageable=PageRequest.of(page,size,Sort.Direction.ASC,"id");
         Specification<CommentEntity> spec;
         if (filterDto.getCommentId()!=null){
             spec=Specification.where(CommentSpecification.id("id",filterDto.getCommentId()));
         }else{
             spec=Specification.where(CommentSpecification.idIsNotNull());
         }

         if (filterDto.getArticleid()!=null){
            spec= spec.and(CommentSpecification.id("article", filterDto.getArticleid()));
         }

         if (filterDto.getProfileId()!=null){
            spec= spec.and(CommentSpecification.id("profile", filterDto.getProfileId()));
         }

         if (filterDto.getFromDate() != null) {
            spec= spec.and(CommentSpecification.greaterThanOrEqualTo(filterDto.getFromDate()));
         }
         if (filterDto.getToDate() != null) {
             spec=spec.and(CommentSpecification.lessThanOrEqualTo(filterDto.getToDate()));
         }

         Page<CommentEntity> entityPage=commentRepository.findAll(spec,pageable);
         List<CommentEntity> entityList=entityPage.getContent();
         List<CommentDto> dtoList=entityList.stream().map(this::toDto).toList();

        return new PageImpl<>(dtoList,pageable,entityPage.getTotalElements());
    }
}
