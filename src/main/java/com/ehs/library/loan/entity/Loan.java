package com.ehs.library.loan.entity;

import com.ehs.library.book.entity.Book;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="loan")
@Getter @Setter
@ToString
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
}
