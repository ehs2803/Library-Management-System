package com.ehs.library.book.dto;

import com.ehs.library.book.constant.BookHopeState;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookHopeUpdateDto {
    private Long id;

    private BookHopeState state;

    private String failReason;
}
