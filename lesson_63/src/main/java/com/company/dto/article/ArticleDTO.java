package com.company.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;
    @NotEmpty(message = "title can not be null")
    private String title;
    @NotEmpty(message = "content is Empty")
    private String content;

    private Integer profileId;

    // status

    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;

}
