package com.ehs.library.roomreservation.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyRoomBookFormDto {
    private Long id;

    @NotBlank(message = "예약날짜는 필수 입력 값입니다.")
    private String bookLocalDate;

    @NotBlank(message = "예약 시작시간은 필수 입력 값입니다.")
    private String time;

    @Min(value = 1, message = "이용시간은 1시간 이상으로 입력하세요.")
    private int useHour;

    @Min(value = 1, message = "예약인원은 1명이상으로 입력하세요.")
    private int personCnt;
}
