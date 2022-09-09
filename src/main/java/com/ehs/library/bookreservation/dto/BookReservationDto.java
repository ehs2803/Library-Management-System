package com.ehs.library.bookreservation.dto;

import com.ehs.library.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookReservationDto {
    private Long id;
    private BookDto book;
    private LocalDateTime regTime;
}
