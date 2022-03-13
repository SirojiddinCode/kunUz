package com.company.repository;

import com.company.entity.EmailHistoryEntity;
import com.company.enums.EmailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update EmailHistoryEntity  e set e.status=:status,e.usedDate=:date where e.id=:id")
    void update(@Param("status") EmailStatus status, @Param("date") LocalDateTime date, @Param("id") Integer id);

    List<EmailHistoryEntity> getAllByCreatedDateBetween(LocalDateTime createdDate1, LocalDateTime createdDate2);

    EmailHistoryEntity findFirstByOrderByCreatedDateDesc();

    Page<EmailHistoryEntity> getAllByStatus(EmailStatus status,Pageable pageable);
}
