package com.company.dto;

import com.company.enums.EmailStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailHistoryDTO {
    private Integer id;
    private String fromAccount;
    private String toAccount;
    private LocalDateTime createdDate;

    private EmailStatus status;
    private LocalDateTime usedDate;
}
