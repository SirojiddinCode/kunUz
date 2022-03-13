package com.company.entity;

import com.company.enums.EmailStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "emailHistory")
public class EmailHistoryEntity extends BaseEntity{
    @Column
    private String fromAccount;
    @Column
    private String toAccount;

    @Enumerated(EnumType.STRING)
    @Column
    private EmailStatus status;
    @Column
    private LocalDateTime usedDate;

}
