package com.company.dto.comment;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class CommentDto {
    private Integer id;
    // id,content,articleId,Profile,CreatedDate (token)
    @NotEmpty(message = "content is empty")
    private String content;
    @NotNull(message = "article id can not be null")
    private Integer articleId;
    private Integer profileId;
    private LocalDateTime createdDate;
}
