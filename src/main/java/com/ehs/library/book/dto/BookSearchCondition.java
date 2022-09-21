package com.ehs.library.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class BookSearchCondition {
    private String name;
    private String isbn;
    private String author;
    private String publisher;
    private Integer year;
}
