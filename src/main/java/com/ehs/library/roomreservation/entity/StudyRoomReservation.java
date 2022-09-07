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

    private LocalDateTime reservation_time;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

    private int use_hour;

    private int person_cnt;

    @Enumerated(EnumType.STRING)
    private ReservationState state;
}
