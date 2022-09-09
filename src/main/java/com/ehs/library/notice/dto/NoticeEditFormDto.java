package com.ehs.library.notice.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class NoticeEditFormDto {
    private Long id;

    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String title;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
