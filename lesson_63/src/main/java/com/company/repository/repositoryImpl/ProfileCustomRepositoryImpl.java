package com.company.repository.repositoryImpl;

import com.company.dto.profile.ProfileFilterDto;
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
public class ProfileCustomRepositoryImpl {
    @Autowired
    private  EntityManager entityManager;

    public  PageImpl filter(int page, int size, ProfileFilterDto filterDto){
        Map<String,Object> params=new HashMap<>();
        StringBuilder builder =new StringBuilder("SELECT p FROM ProfileEntity p ");
        StringBuilder builderCount =new StringBuilder("SELECT count(p) FROM ProfileEntity p ");
        if (filterDto.getId()!=null){
            builder.append("WHERE p.id=:id ");
            builderCount.append("WHERE p.id=:id ");
            params.put("id",filterDto.getId());
        }else{
            builder.append("WHERE p.id is not null ");
            builderCount.append("WHERE p.id is not null ");
        }

        if (!(filterDto.getName()==null||filterDto.getName().isEmpty())){
            builder.append("AND p.name=:name ");
            builderCount.append("AND p.name=:name ");
            params.put("name",filterDto.getName());
        }
        if (!(filterDto.getSurname()==null||filterDto.getSurname().isEmpty())){
            builder.append("AND p.surname=:surname ");
            builderCount.append("AND p.surname=:surname ");
            params.put("surname",filterDto.getSurname());
        }
        if (!(filterDto.getEmail()==null||filterDto.getEmail().isEmpty())){
            builder.append("AND p.email=:email ");
            builderCount.append("AND p.email=:email ");
            params.put("email",filterDto.getEmail());
        }
        if (filterDto.getStatus()!=null){
            builder.append("AND p.status=:status ");
            builderCount.append("AND p.status=:status ");
            params.put("status",filterDto.getStatus());
        }
        if (filterDto.getRole()!=null){
            builder.append("AND p.role=:role ");
            builderCount.append("AND p.role=:role ");
            params.put("role",filterDto.getRole());
        }

        Query query=entityManager.createQuery(builder.toString());
        query.setFirstResult((page-1)*size);
        query.setMaxResults(size);

        for (Map.Entry<String, Object> entryset: params.entrySet()){
            query.setParameter(entryset.getKey(),entryset.getValue());
        }

        List<CommentEntity> entityList=query.getResultList();
         query=entityManager.createQuery(builderCount.toString());

        for (Map.Entry<String,Object> entry:params.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        Long totalElement= (Long) query.getSingleResult();
        return new PageImpl(entityList, PageRequest.of(page,size) ,totalElement);

    }
}
