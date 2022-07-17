package com.ehs.library.bookhope.dto;

import com.ehs.library.bookhope.constant.BookHopeState;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookHopeUpdateDto {
    private Long id;

    private BookHopeState state;

    private String failReason;
}
