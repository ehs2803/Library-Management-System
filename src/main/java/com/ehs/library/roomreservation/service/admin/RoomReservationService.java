package com.ehs.library.roomreservation.service.admin;

import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomAddFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("admin.RoomReservationService")
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;

    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    public StudyRoom registerStudyRoom(StudyRoomAddFormDto studyRoom){
        StudyRoom validStudyRoom = studyRoomRepository.findByName(studyRoom.getName());
        if(validStudyRoom != null){
            throw new IllegalStateException("존재하는 스터디룸 이름입니다.");
        }

        StudyRoom registerStudyRoom = StudyRoom.builder()
                .name(studyRoom.getName())
                .location(studyRoom.getLocation())
                .capacity(studyRoom.getCapacity())
                .state(StudyRoomState.AVAILABLE)
                .build();

        return studyRoomRepository.save(registerStudyRoom);
    }
}
