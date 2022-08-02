package com.ehs.library.base.schedul;

import com.ehs.library.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class BatchScheduler {
    private final LoanService loanService;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTaskUsingCronExpression() {
        loanService.updateLoanState();
    }
}
