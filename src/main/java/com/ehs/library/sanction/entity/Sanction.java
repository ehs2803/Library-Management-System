package com.ehs.library.sanction.entity;

import com.ehs.library.base.entity.BaseTimeEntity;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.constant.SanctionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="sanction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sanction extends BaseTimeEntity {
    @Id
    @Column(name="sanction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "sanction_day")
    private int sanctionDay;

    @Enumerated(EnumType.STRING)
    private SanctionType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_reservation_id")
    private StudyRoomReservation studyRoomReservation;
}
