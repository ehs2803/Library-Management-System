package com.ehs.library.dto;

import com.ehs.library.constant.BookState;
import com.ehs.library.entity.Book;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookFormDto {
    private Long id;

    @NotBlank(message = "책이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "ISBN은 필수 입력 값입니다.")
    private String isbn;

    @NotBlank(message = "저자는 필수 입력 값입니다.")
    private String author;

    @NotBlank(message = "출판사는 필수 입력 값입니다.")
    private String publisher;

    @NotNull(message = "상품명은 필수 입력 값입니다.")
    private Integer year;

    @Range(min = 1, message = "상품가격은 1원이상입니다.")
    private int price;

    @Range(min = 1, message = "상품페이지는 1이상입니다.")
    private int page;

    private String content;

    @NotBlank(message = "등록번호는 필수 입력 값입니다.")
    private String register_numer;

    @NotBlank(message = "청구기호는 필수 입력 값입니다.")
    private String symbol;

    @NotBlank(message = "대분류는 필수 입력 값입니다.")
    private String classification;

    @NotBlank(message = "소분류는 필수 입력 값입니다.")
    private String classification_detail;

    private BookState state;

    private String location;

    private BookImgDto bookImgDto;

    private Long bookImgId;

    private static ModelMapper modelMapper = new ModelMapper();

    public Book createBook(){
        return modelMapper.map(this, Book.class);
    }

    public static BookFormDto of(Book book){
        return modelMapper.map(book, BookFormDto.class);
    }
}
