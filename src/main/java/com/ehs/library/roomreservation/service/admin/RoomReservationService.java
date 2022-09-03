package com.ehs.library.roomreservation.service.admin;

import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("admin.RoomReservationService")
@Transactional
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;

    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    public StudyRoom registerStudyRoom(StudyRoomFormDto studyRoom){
        StudyRoom validStudyRoom = studyRoomRepository.findByName(studyRoom.getName());
        if(validStudyRoom != null){
            throw new IllegalStateException("존재하는 스터디룸 이름입니다.");
        }

        StudyRoom registerStudyRoom = new StudyRoom();
        registerStudyRoom.setName(studyRoom.getName());
        registerStudyRoom.setLocation(studyRoom.getLocation());
        registerStudyRoom.setCapacity(studyRoom.getCapacity());
        registerStudyRoom.setState(StudyRoomState.AVAILABLE);

        return studyRoomRepository.save(registerStudyRoom);
    }

    public Long changeStateStudyRoom(Long id){
        StudyRoom studyRoom = studyRoomRepository.findById(id).get();
        if(studyRoom.getState().toString().equals("AVAILABLE")){
            studyRoom.setState(StudyRoomState.CLOSE);
        }
        else{
            studyRoom.setState(StudyRoomState.AVAILABLE);
        }
        return studyRoom.getId();
    }

    public StudyRoomFormDto getStudyRoom(Long id){
        StudyRoom studyRoom = studyRoomRepository.findById(id).get();
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .id(studyRoom.getId())
                .name(studyRoom.getName())
                .location(studyRoom.getLocation())
                .capacity(studyRoom.getCapacity())
                .build();
        return studyRoomFormDto;
    }

    public StudyRoom editStudyRoom(StudyRoomFormDto studyRoomFormDto){
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomFormDto.getId()).get();
        studyRoom.setName(studyRoomFormDto.getName());
        studyRoom.setLocation(studyRoomFormDto.getLocation());
        studyRoom.setCapacity(studyRoomFormDto.getCapacity());
        return studyRoom;
    }
}
