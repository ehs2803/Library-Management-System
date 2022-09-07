package com.ehs.library.roomreservation.service.user;

import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import com.ehs.library.roomreservation.repository.StudyRoomReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service("user.RoomReservationService")
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomReservationRepository studyRoomReservationRepository;
    private final MemberRepository memberRepository;

    public StudyRoom findById(Long id){
        return studyRoomRepository.findById(id).get();
    }

    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    public StudyRoom getStudyRoomFetchJoin(Long id){
        return studyRoomRepository.findByIdFetchJoin(id);
    }

    public List<StudyRoomReservation> findByMemberFetchJoinRoom(String email){
        return studyRoomReservationRepository.findByMemberFetchJoinRoom(memberRepository.findByEmail(email));
    }

    public Long reservationStudyRoom(String email, StudyRoomBookFormDto studyRoomBookFormDto){
        Member member = memberRepository.findByEmail(email);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomBookFormDto.getId()).get();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        String inputDate = studyRoomBookFormDto.getBookLocalDate()+"-"+studyRoomBookFormDto.getTime();
        LocalDateTime localDateTime = LocalDateTime.parse(inputDate, formatter);

        StudyRoomReservation studyRoomReservation = StudyRoomReservation.builder()
                .room(studyRoom)
                .member(member)
                .use_hour(studyRoomBookFormDto.getUse_hour())
                .person_cnt(studyRoomBookFormDto.getPerson_cnt())
                .reservation_time(localDateTime)
                .state(ReservationState.WAIT)
                .build();
        studyRoomReservationRepository.save(studyRoomReservation);
        return studyRoomReservation.getId();
    }

    public void studyRoomStateSetCancel(Long id){
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();
        studyRoomReservation.setState(ReservationState.CANCEL);
    }
}
