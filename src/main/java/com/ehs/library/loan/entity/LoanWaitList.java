package com.ehs.library.loan.entity;

import com.ehs.library.book.entity.Book;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="loan_wait_list")
@Getter @Setter
public class LoanWaitList {
    @Id
    @Column(name="loan_wait_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public LoanWaitList(Member member, Book book){
        this.member = member;
        this.book = book;
    }

    public LoanWaitList() {

    }
}
