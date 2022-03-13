package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommentSpecification {

    public static Specification<CommentEntity> id(String field,Integer id){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field),id));
    }
    public static Specification<CommentEntity> idIsNotNull(){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("id")));
    }
    public static Specification<CommentEntity> greaterThanOrEqualTo(LocalDate fromDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), LocalDateTime.of(fromDate, LocalTime.MIN));
        });
    }
    public static Specification<CommentEntity> lessThanOrEqualTo(LocalDate toDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),LocalDateTime.of(toDate,LocalTime.MAX)));
    }

}
