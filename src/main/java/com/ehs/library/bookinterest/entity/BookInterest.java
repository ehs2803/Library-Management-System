package com.ehs.library.bookinterest.entity;

import com.ehs.library.book.entity.Book;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="book_interest")
@Getter @Setter
public class BookInterest {
    @Id
    @Column(name="book_interest_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    public BookInterest(){

    }

    public BookInterest(Book book, Member member, LocalDateTime regTime) {
        this.book = book;
        this.member = member;
        this.regTime = regTime;
    }
}
