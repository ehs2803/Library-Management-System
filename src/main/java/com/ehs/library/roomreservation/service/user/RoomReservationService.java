package com.ehs.library.roomreservation.service.user;

import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("user.RoomReservationService")
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;

    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }
}
