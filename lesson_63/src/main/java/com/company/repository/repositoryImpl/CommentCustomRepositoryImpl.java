package com.company.repository.repositoryImpl;

import com.company.dto.comment.CommentFilterDto;
import com.company.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    public PageImpl filter(int page, int size, CommentFilterDto filterDto){
        Map<String,Object> params=new HashMap<>();
        StringBuilder builder =new StringBuilder("SELECT c FROM CommentEntity c ");
        StringBuilder builderCount =new StringBuilder("SELECT count(c) FROM CommentEntity c ");
        if (filterDto.getCommentId()!=null){
            builder.append("WHERE c.id=:id ");
            builderCount.append("WHERE c.id=:id ");
            params.put("id",filterDto.getCommentId());
        }else{
            builder.append("WHERE c.id is not null ");
            builderCount.append("WHERE c.id is not null ");
        }

        if (filterDto.getArticleid()!=null){
            builder.append("AND c.article.id=:articleId ");
            builderCount.append("AND c.article.id=:articleId ");
            params.put("articleId",filterDto.getArticleid());
        }
        if (filterDto.getProfileId()!=null){
            builder.append("AND c.profile.id=:profileId ");
            builderCount.append("AND c.profile.id=:profileId ");
            params.put("profileId",filterDto.getProfileId());
        }
        if (filterDto.getFromDate()!=null){
            builder.append(" and a.createdDate >=:fromDate ");
            builderCount.append(" and a.createdDate >=:fromDate ");
            params.put("fromDate",filterDto.getFromDate());
        }

        if (filterDto.getToDate()!=null){
            builder.append(" and a.createdDate >=:fromDate ");
            builderCount.append(" and a.createdDate >=:fromDate ");
            params.put("fromDate",filterDto.getToDate());
        }

        Query query=entityManager.createQuery(builder.toString());
        query.setFirstResult((page-1)*size);
        query.setMaxResults(size);

        for (Map.Entry<String, Object> entryset: params.entrySet()){
            query.setParameter(entryset.getKey(),entryset.getValue());
        }

        List<CommentEntity> entityList=query.getResultList();
        Query countquery=entityManager.createQuery(builderCount.toString());

        for (Map.Entry<String,Object> entry:params.entrySet()){
            countquery.setParameter(entry.getKey(),entry.getValue());
        }
        Long totalElement= (Long) countquery.getSingleResult();
        return new PageImpl(entityList, PageRequest.of(page,size) ,totalElement);

    }
    /*public PageImpl filter2(int page, int size, CommentFilterDto filterDto){

    }*/
}
