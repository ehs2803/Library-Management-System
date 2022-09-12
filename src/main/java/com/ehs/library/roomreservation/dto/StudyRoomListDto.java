package com.ehs.library.roomreservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyRoomListDto {
    private Long id;
    private String name;
    private String location;
    private int capacity;
    private String state;
}
