package com.ehs.library.loan.dto;

import com.ehs.library.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanDto {
    Long id;
    BookDto book;
}
