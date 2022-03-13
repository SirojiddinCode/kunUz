package com.company.dto.comment;

import com.company.enums.OrderBy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.jaxb.OrderAdapter;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CommentFilterDto {
    private Integer commentId;
    private Integer profileId;
    private Integer articleid;

    private LocalDate fromDate;
    private LocalDate toDate;
}
