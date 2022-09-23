package com.ehs.library.roomreservation.entity;

import com.ehs.library.base.entity.BaseEntity;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="study_room_reservation")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRoomReservation extends BaseEntity {
    @Id
    @Column(name="study_room_reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    private StudyRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JoinColumn(name = "reservation_time")
    private LocalDateTime reservationTime;

    @JoinColumn(name = "start_time")
    private LocalDateTime startTime;

    @JoinColumn(name = "end_time")
    private LocalDateTime endTime;

    @JoinColumn(name = "use_hour")
    private int useHour;

    @JoinColumn(name = "person_cnt")
    private int personCnt;

    @Enumerated(EnumType.STRING)
    private ReservationState state;
}
