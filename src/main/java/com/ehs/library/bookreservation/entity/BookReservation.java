package com.ehs.library.bookreservation.entity;

import com.ehs.library.book.entity.Book;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="book_reservation")
@Getter @Setter
@ToString
public class BookReservation {
    @Id
    @Column(name="book_reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int remainDay;

    private int sequence;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime resTime;

    public BookReservation() {
    }

    public BookReservation(Book book, Member member, int remainDay, int sequence, LocalDateTime resTime) {
        this.book = book;
        this.member = member;
        this.remainDay = remainDay;
        this.sequence = sequence;
        this.resTime = resTime;
    }
}
