package com.ehs.library.loan.entity;

import com.ehs.library.book.entity.Book;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="loan")
@Getter @Setter
@NoArgsConstructor
public class Loan {
    @Id
    @Column(name="loan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime startLoanTime;

    private LocalDateTime returnTime;

    @Enumerated(EnumType.STRING)
    private LoanState loanState;

    private int remainDay;

    private int useExtensionCnt;

    private int overdueDay;

    public Loan(Book book, Member member, LoanState loanState, LocalDateTime startLoanTime) {
        this.book = book;
        this.member = member;
        this.loanState = loanState;
        this.startLoanTime = startLoanTime;
    }

//    @PrePersist
//    void setLoanTime(){
//        this.startLoanTime=LocalDateTime.now();
//    }
}
