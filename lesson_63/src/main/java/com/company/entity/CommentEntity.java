package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity{
    // id,content,articleId,Profile,CreatedDate (token)
    @Column
    private String content;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "articleId")
    private ArticleEntity article;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId")
    private ProfileEntity profile;

}
