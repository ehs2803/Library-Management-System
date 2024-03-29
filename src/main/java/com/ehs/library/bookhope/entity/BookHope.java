package com.ehs.library.bookhope.entity;

import com.ehs.library.base.entity.BaseTimeEntity;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="book_hope")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookHope extends BaseTimeEntity {
    @Id
    @Column(name="book_hope_id")
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

    @Enumerated(EnumType.STRING)
    private BookHopeState state;

    private String failReason;

    private LocalDateTime rejectTime;

    private LocalDateTime allowTime;

    private LocalDateTime shippingTime;

    private LocalDateTime arrangeTime;

    private LocalDateTime completeTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member user;

}
