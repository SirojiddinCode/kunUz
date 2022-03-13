package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ProfileSpecification {

    public static Specification<ProfileEntity> profileStatus(ProfileStatus status){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"),status));
    }
    public static Specification<ProfileEntity> id(String field,Integer id){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field),id));
    }
    public static Specification<ProfileEntity> role(ProfileRole role){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"),role));
    }
    public static Specification<ProfileEntity> stringField(String field,String value){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<ProfileEntity> greaterThanOrEqualTo(LocalDate fromDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), LocalDateTime.of(fromDate, LocalTime.MIN));
        });
    }
    public static Specification<ProfileEntity> lessThanOrEqualTo(LocalDate toDate) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),LocalDateTime.of(toDate,LocalTime.MAX)));
    }
}
