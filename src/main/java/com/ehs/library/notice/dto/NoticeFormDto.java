package com.ehs.library.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeFormDto {
    private Long id;

    private String title;

    private String content;

    private int hit;

    private LocalDateTime createTime;

    @Builder
    public NoticeFormDto(Long id, String title, String content, int hit, LocalDateTime createTime){
        this.id=id;
        this.title=title;
        this.content=content;
        this.hit=hit;
        this.createTime=createTime;
    }
}