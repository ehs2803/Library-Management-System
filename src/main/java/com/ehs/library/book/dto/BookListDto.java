package com.ehs.library.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BookListDto {
    private Long id;
    private String location;
    private String state;
    private String symbol;
    private String register_numer;
    private LocalDate return_day;
}

