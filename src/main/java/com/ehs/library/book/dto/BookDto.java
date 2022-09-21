package com.ehs.library.book.dto;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.BookImg;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookDto {
    private Long id;
    private String name;
    private String author;
    private String publisher;
    private int year;
    private String content;
    private String symbol;
    private String register_numer;
    private String state;
    private String location;
    private BookImgSimpleDto bookImg;
    private String isbn;

//    @QueryProjection
//    public BookDto(Long id, String name,)
}
