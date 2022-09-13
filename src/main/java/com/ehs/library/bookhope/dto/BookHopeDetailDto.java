package com.ehs.library.bookhope.dto;

import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookHopeDetailDto {
    private Long id;

    private String name;

    private String isbn;

    private String author;

    private String publisher;

    private int year;

    private String state;

    private String failReason;

    private LocalDateTime rejectTime;

    private LocalDateTime allowTime;

    private LocalDateTime shippingTime;

    private LocalDateTime arrangeTime;

    private LocalDateTime completeTime;
}
