package com.ehs.library.base.schedul;

import com.ehs.library.bookreservation.service.BookReservationService;
import com.ehs.library.loan.service.LoanService;
import com.ehs.library.sanction.service.SanctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class BatchScheduler {
    private final LoanService loanService;
    private final BookReservationService bookReservationService;
    private final SanctionService sanctionService;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTaskUsingCronExpression() {

        loanService.updateLoanState();

        sanctionService.decreaseRemainDay(); // 제재 일수 -1 하기

        bookReservationService.decreaseRemainDay(); // 예약도서 remainday -1 하기
    }
}
