package com.ehs.library.roomreservation.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyRoomFormDto {
    private Long id;

    @NotBlank(message = "스터디룸 이름은 필수 입력 값입니다.")
    private String name;

    @Min(value = 1, message = "수용인원은 1명이상으로 입력하세요.")
    private int capacity;

    @NotBlank(message = "스터디룸 이름은 필수 입력 값입니다.")
    private String location;
}
