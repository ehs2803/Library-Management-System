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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service("user.RoomReservationService")
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomReservationRepository studyRoomReservationRepository;
    private final MemberRepository memberRepository;

    // id로 스터디룸 조회
    public StudyRoom findById(Long id){
        return studyRoomRepository.findById(id).get();
    }

    // 모든 스터디룸 조회
    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    // 스터디룸 id State가 wait or allow인 것 reservations까지 fetch join
    public StudyRoom findByIdAndStateWaitOrAllowFetchJoi(Long id){
        return studyRoomRepository.findByIdAndStateWaitOrAllowFetchJoin(id);
    }

    // 특정 유저의 스터디룸예약 목록 조회
    public List<StudyRoomReservation> findByMemberFetchJoinRoom(String email){
        return studyRoomReservationRepository.findByMemberFetchJoinRoom(memberRepository.findByEmail(email));
    }

    // 스터디룸 예약하기
    public Long reservationStudyRoom(String email, StudyRoomBookFormDto studyRoomBookFormDto){
        Member member = memberRepository.findByEmail(email);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomBookFormDto.getId()).get();

        // 입력 예약날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        String inputDate = studyRoomBookFormDto.getBookLocalDate()+"-"+studyRoomBookFormDto.getTime();
        LocalDateTime localDateTime = LocalDateTime.parse(inputDate, formatter);

        // 제재

        // 예약시간이 겹치는 경우
//        List<StudyRoomReservation> validateTime =
//                studyRoomReservationRepository.findByRoomAndReservation_time(studyRoom, LocalDate.now());
//        System.out.println(validateTime.size());

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

    // 스터디룸 예약 취소
    public void studyRoomStateSetCancel(Long id) throws Exception {
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();
        // 예약날짜와 현재날짜가 같을 경우
        if(ChronoLocalDate.from(studyRoomReservation.getReservation_time()).isEqual(LocalDate.now())){
            throw new Exception("당일 예약 취소는 불가능합니다.");
        }
        studyRoomReservation.setState(ReservationState.CANCEL);
    }
}
