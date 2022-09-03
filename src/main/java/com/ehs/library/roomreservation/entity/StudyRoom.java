package com.ehs.library.roomreservation.entity;

import com.ehs.library.base.entity.BaseEntity;
import com.ehs.library.book.constant.BookState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name="study_room")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyRoom extends BaseEntity {
    @Id
    @Column(name="study_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int capacity;

    private String location;

    @Enumerated(EnumType.STRING)
    private StudyRoomState state;

    @OneToMany(mappedBy = "room")
    private List<StudyRoomReservation> reservations = new ArrayList<>();
}
