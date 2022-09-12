package com.ehs.library.roomreservation.dto;

import com.ehs.library.roomreservation.constant.StudyRoomState;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class StudyRoomDto {
    private String name;
    private String location;
    private int capacity;
}
