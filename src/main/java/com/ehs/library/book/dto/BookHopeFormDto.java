package com.ehs.library.book.dto;

import com.ehs.library.book.constant.BookHopeState;
import com.ehs.library.book.constant.BookState;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookHopeFormDto {
    private Long id;

    @NotBlank(message = "책이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "ISBN은 필수 입력 값입니다.")
    private String isbn;

    @NotBlank(message = "저자는 필수 입력 값입니다.")
    private String author;

    @NotBlank(message = "출판사는 필수 입력 값입니다.")
    private String publisher;

    @NotNull(message = "출판년도는 필수 입력 값입니다.")
    private Integer year;

    private BookHopeState state;

}
