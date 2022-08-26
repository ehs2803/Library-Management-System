package com.ehs.library.roomreservation.entity;

import com.ehs.library.base.entity.BaseEntity;
import com.ehs.library.book.constant.BookState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="study_room")
@Getter @Setter
@ToString
public class StudyRoom extends BaseEntity {
    @Id
    @Column(name="study_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private StudyRoomState state;
}
