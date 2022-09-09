package com.ehs.library.book.dto;

import com.ehs.library.book.constant.BookState;
import com.ehs.library.book.entity.BookImg;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookDto {
    private String name;
    private String author;
    private String publisher;
    private int year;
    private String symbol;
    private String state;
    private String location;
    private BookImgSimpleDto bookImg;
}
