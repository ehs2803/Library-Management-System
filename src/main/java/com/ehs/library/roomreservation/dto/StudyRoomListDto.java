package com.ehs.library.roomreservation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomListDto {
    private Long id;
    private String name;
    private String location;
    private int capacity;
    private String state;
}
