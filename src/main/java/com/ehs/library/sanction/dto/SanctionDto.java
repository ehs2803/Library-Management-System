package com.ehs.library.sanction.dto;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.loan.dto.LoanDto;
import com.ehs.library.roomreservation.dto.StudyRoomReservationDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanctionDto {
    private int sanctionDay;
    private String type;
    private LocalDateTime regTime;
    private LoanDto loan;
    private StudyRoomReservationDto studyRoomReservation;
}
