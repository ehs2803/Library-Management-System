package com.ehs.library.book.dto;

import com.ehs.library.book.entity.BookImg;
import org.modelmapper.ModelMapper;

public class BookImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BookImgDto of(BookImg bookImg){
        return modelMapper.map(bookImg, BookImgDto.class);
    }
}
