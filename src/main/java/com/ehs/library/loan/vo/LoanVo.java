package com.ehs.library.loan.vo;

import com.ehs.library.book.vo.BookVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanVo {
    private Long loan_id;
    private String loan_state;
    private LocalDateTime start_loan_time;
    private Long book_id;
    private int remain_day;
    private int overdue_day;
    private int use_extension_cnt;

    private BookVo bookVo;
}
