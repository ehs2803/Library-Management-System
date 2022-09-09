package com.ehs.library.notice.dto;

import com.ehs.library.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime regTime;
}
