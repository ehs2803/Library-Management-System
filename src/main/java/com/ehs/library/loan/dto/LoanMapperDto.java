package com.ehs.library.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanMapperDto {
    Long loan_id;
    String loan_state;
    LocalDateTime start_loan_time;
    Long book_id;
    int remain_day;
    int overdue_day;
    int use_extension_cnt;

    String name;
}
