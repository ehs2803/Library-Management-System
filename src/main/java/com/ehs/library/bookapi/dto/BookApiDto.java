package com.ehs.library.bookapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookApiDto {
    private String title;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String description;
    private String link;
}
