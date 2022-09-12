package com.ehs.library.roomreservation.dto;

import com.ehs.library.member.dto.MemberSimpleDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.entity.StudyRoom;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyRoomReservationDto {
    private Long id;
    private LocalDateTime reservation_time;
    private int use_hour;
    private int person_cnt;
    private StudyRoomDto room;
    private String createdBy;
    private LocalDateTime regTime;
    private String state;
    //private MemberSimpleDto member;
}
