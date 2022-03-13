package com.company.service;

import com.company.dto.EmailHistoryDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.enums.EmailStatus;
import com.company.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    private Integer emailHistoryId;


    public void sendEmail(String toAccount, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAccount);
        message.setFrom("sirojiddinmamatqulov0228@gmail.com");
        message.setSubject(title);
        message.setText(text);
        javaMailSender.send(message);
        createHistory(message, toAccount);
    }

    public void createHistory(SimpleMailMessage message, String toAccount) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setFromAccount(message.getFrom());
        entity.setToAccount(toAccount);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(EmailStatus.NOT_USED);
        emailHistoryRepository.save(entity);
        emailHistoryId = entity.getId();
    }

    public void updateEmailHistory() {
        emailHistoryRepository.update(EmailStatus.USED, LocalDateTime.now(), emailHistoryId);
    }

    public EmailHistoryEntity get(Integer id) {
        return emailHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("EmailHistory not found"));
    }

    public PageImpl<EmailHistoryDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
        Page<EmailHistoryEntity> pages = emailHistoryRepository.findAll(pageable);
        long totalElement = pages.getTotalElements();
        Integer p = pages.getTotalPages();
        List<EmailHistoryEntity> entityList = pages.getContent();
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        entityList.forEach(e -> dtoList.add(toDto(e)));
        return new PageImpl<>(dtoList, pageable, totalElement);
    }

    public EmailHistoryDTO toDto(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setFromAccount(entity.getFromAccount());
        dto.setToAccount(entity.getToAccount());
        dto.setStatus(entity.getStatus());
        dto.setUsedDate(entity.getUsedDate());
        return dto;
    }

    public List<EmailHistoryDTO> getToday() {
        LocalDateTime date1 = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime date2 = LocalDateTime.now();
        List<EmailHistoryEntity> entityList = emailHistoryRepository.getAllByCreatedDateBetween(date1, date2);
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        entityList.forEach(e -> dtoList.add(toDto(e)));
        return dtoList;
    }

    public EmailHistoryDTO getLast() {
        return toDto(emailHistoryRepository.findFirstByOrderByCreatedDateDesc());
    }

    public PageImpl<EmailHistoryDTO> getbyStatus(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
        Page<EmailHistoryEntity> pages = emailHistoryRepository.getAllByStatus(EmailStatus.NOT_USED,pageable);
        long totalElement = pages.getTotalElements();
        List<EmailHistoryEntity> entityList = pages.getContent();
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        entityList.forEach(e -> dtoList.add(toDto(e)));
        return new PageImpl<EmailHistoryDTO>(dtoList, pageable, totalElement);
    }
}
