package com.ehs.library.bookhope.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookHopeMapperDto {
    private Long book_hope_id;
    private String name;
    private String isbn;
    private LocalDateTime reg_time;
    private String state;
}
