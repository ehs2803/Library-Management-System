package com.ehs.library.loan.dto;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.loan.constant.LoanState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class LoanHistoryDto {
    private BookDto book;
    private LocalDateTime startLoanTime;
    private LocalDateTime returnTime;
    private String loanState;
    private int useExtensionCnt;
    private int overdueDay;
}
