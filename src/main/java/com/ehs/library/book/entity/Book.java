package com.ehs.library.book.entity;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.base.entity.BaseEntity;
import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Table(name="book")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {
    @Id
    @Column(name="book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int year;

    private int price;

    private int page;

    @Lob
    private String content;

    @Column(unique = true, nullable = false)
    private String register_numer;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String classification;

    @Column(nullable = false)
    private String classification_detail;

    @Enumerated(EnumType.STRING)
    private BookState state;

    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "book")
    private BookImg bookImg;

    private int loanCnt;

    public void updateBook(BookFormDto itemFormDto){

    }
}
