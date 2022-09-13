package com.ehs.library.bookhope.dto;

import com.ehs.library.member.dto.MemberSimpleDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BookHopeDto {
    private Long id;
    private String name;
    private LocalDateTime regTime;
    private MemberSimpleDto user;
}
