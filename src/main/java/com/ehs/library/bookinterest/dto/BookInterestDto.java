package com.ehs.library.bookinterest.dto;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.book.entity.Book;
import com.ehs.library.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookInterestDto {
    private Long id;
    private BookDto book;
    private LocalDateTime regTime;
}
