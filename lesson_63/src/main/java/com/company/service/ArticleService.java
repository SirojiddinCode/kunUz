package com.company.service;

import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFilterDto;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.repositoryImpl.ArticleCustomRepositoryImpl;
import com.company.repository.ArticleRepository;
import com.company.spec.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;

    public ArticleDTO create(ArticleDTO dto, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new BadRequestException("Title can not be null");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new BadRequestException("Content can not be null");
        }


        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle()); // null
        entity.setContent(dto.getContent());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfile(profileEntity);
        entity.setStatus(ArticleStatus.CREATED);

        articleRepository.save(entity);
        dto.setId(entity.getId());
        return toDto(entity);
    }

    public void publish(Integer articleId, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        ArticleEntity entity = get(articleId);
        if (entity.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new BadRequestException("Already Published");
        }
        entity.setStatus(ArticleStatus.PUBLISHED);
        entity.setPublishedDate(LocalDateTime.now());
        entity.setPublisher(profileEntity);
        articleRepository.save(entity);
    }

    public ArticleEntity get(Integer id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
    }

    public ArticleDTO getById(Integer id) {
        return toDto(get(id));
    }

    public boolean existsById(Integer id) {
        return articleRepository.existsById(id);
    }


    public PageImpl<ArticleDTO> filter(int page, int size, ArticleFilterDto filterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<ArticleEntity> spec = null;
        if (filterDTO.getStatus() != null) {
            spec = Specification.where(ArticleSpecification.status(filterDTO.getStatus()));
        } else {
            spec = Specification.where(ArticleSpecification.status(ArticleStatus.PUBLISHED));
        }

        if (filterDTO.getTitle() != null) {
           spec= spec.and(ArticleSpecification.title(filterDTO.getTitle()));
        }
        if (filterDTO.getArticleId() != null) {
            spec=spec.and(ArticleSpecification.equal("article", filterDTO.getArticleId()));
        }
        if (filterDTO.getProfileId() != null) {
            spec=spec.and(ArticleSpecification.equal("profile", filterDTO.getProfileId()));
        }

        if (filterDTO.getFromDate() != null) {
            spec=spec.and(ArticleSpecification.greaterThanOrEqualTo(filterDTO.getFromDate()));
        }
        if (filterDTO.getToDate() != null) {
            spec=spec.and(ArticleSpecification.lessThanOrEqualTo(filterDTO.getToDate()));
        }
        Page<ArticleEntity> articlePage = articleRepository.findAll(spec, pageable);
        List<ArticleEntity> entityList = articlePage.toList();
        List<ArticleDTO> dtoList = entityList.stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList, pageable, articlePage.getTotalElements());
        /*  Specification<ArticleEntity> title=(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("title"),filterDTO.getTitle())));

        Specification<ArticleEntity> idSpec=(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"),filterDTO.getArticleId())));

        Specification<ArticleEntity> spec=Specification.where(title);
        spec.and(idSpec);

        Page<ArticleEntity> articlePage=articleRepository.findAll(spec,pageable);

        PageImpl<ArticleEntity> entityPage = articleCustomRepository.filter(page, size, filterDTO);

        List<ArticleDTO> articleDTOList = entityPage.stream().map(this::toDto).collect(Collectors.toList());

        return new PageImpl<>(articleDTOList, entityPage.getPageable(), entityPage.getTotalElements());*/

    }


    public ArticleDTO update(Integer articleId, Integer userId, ArticleDTO dto) {
        ArticleEntity entity = get(articleId);
        if (!entity.getProfile().getId().equals(userId)) {
            throw new BadRequestException("Not Owner");
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        articleRepository.save(entity);
        return toDto(entity);
    }

    public ArticleDTO toDto(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setProfileId(entity.getProfile().getId());
        return dto;
    }

    public void delete(Integer articleId) {
        if (existsById(articleId)) {
            articleRepository.deleteById(articleId);
        } else {
            throw new BadRequestException("Not Found");
        }
    }

    public void blockArticle(Integer articleId) {
        ArticleEntity entity = get(articleId);
        entity.setStatus(ArticleStatus.BLOCKED);
        articleRepository.save(entity);
    }

    public List<ArticleDTO> getAll() {
        List<ArticleDTO> dtoList = articleRepository.findAll().stream().map(this::toDto).toList();
        return dtoList;
    }
}
