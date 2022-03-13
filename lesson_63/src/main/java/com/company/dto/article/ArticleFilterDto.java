package com.company.dto.article;

import com.company.enums.ArticleStatus;
import com.company.enums.OrderBy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class ArticleFilterDto {
    private String title;
    private Integer profileId;
    private Integer articleId;
    private ArticleStatus status;
    private String orederByfield;
    private Sort.Direction order;

    private LocalDate fromDate;
    private LocalDate toDate;
}
