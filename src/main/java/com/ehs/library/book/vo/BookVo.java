package com.ehs.library.book.vo;

import lombok.Data;

@Data
public class BookVo {
    private Long book_id;
    private String name;

    private BookImgVo bookImgVo;
}
