package com.ehs.library.entity;

import com.ehs.library.constant.BookState;
import com.ehs.library.constant.Role;
import com.ehs.library.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="book")
@Getter @Setter
@ToString
public class Book extends BaseEntity{
    @Id
    @Column(name="book_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
