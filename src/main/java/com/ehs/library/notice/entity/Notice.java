package com.ehs.library.notice.entity;

import com.ehs.library.base.entity.BaseEntity;
import com.ehs.library.book.entity.Book;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="notice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Notice extends BaseEntity {

    @Id
    @Column(name="notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

//    @Column(columnDefinition = "TEXT")
    @Lob
    private String content;

    private int hit;

}