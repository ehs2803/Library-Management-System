package com.ehs.library.roomreservation.service.user;

import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.exception.RoomReservationOverlapException;
import com.ehs.library.roomreservation.exception.RoomReservationSanctionException;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import com.ehs.library.roomreservation.repository.StudyRoomReservationRepository;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.constant.SanctionType;
import com.ehs.library.sanction.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service("user.RoomReservationService")
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomReservationRepository studyRoomReservationRepository;
    private final MemberRepository memberRepository;

    // id로 스터디룸 조회
    @Transactional(readOnly = true)
    public StudyRoom findById(Long id){
        return studyRoomRepository.findById(id).get();
    }

    // 모든 스터디룸 조회
    @Transactional(readOnly = true)
    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    // 스터디룸 id State가 wait or allow인 것 reservations까지 fetch join
    @Transactional(readOnly = true)
    public StudyRoom findByIdAndStateWaitOrAllowFetchJoi(Long id){
        return studyRoomRepository.findByIdAndStateWaitOrAllowFetchJoin(id);
    }

    // 특정 유저의 스터디룸예약 목록 조회
    @Transactional(readOnly = true)
    public List<StudyRoomReservation> findByMemberFetchJoinRoom(String email){
        return studyRoomReservationRepository.findByMemberFetchJoinRoom(memberRepository.findByEmail(email));
    }

    // 스터디룸 예약하기
    public Long reservationStudyRoom(String email, StudyRoomBookFormDto studyRoomBookFormDto) {
        Member member = memberRepository.findByEmail(email);
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomBookFormDto.getId()).get();

        // 입력 예약날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        String inputDate = studyRoomBookFormDto.getBookLocalDate()+"-"+studyRoomBookFormDto.getTime();
        LocalDateTime localDateTime = LocalDateTime.parse(inputDate, formatter);

        // 제재
//        if(member.getSanctionStudyRoomDay()>0){
//            throw new RoomReservationSanctionException("현재 제재중입니다. 스터디룸 예약이 불가능합니다.");
//        }
//
//        // 예약시간이 겹치는 경우
//        List<StudyRoomReservation> validateTime =
//                studyRoomReservationRepository.findByRoomAndReservationTimeEquals(studyRoom, LocalDate.now());
//        Set<Integer> useHour = new HashSet<>();
//        for(StudyRoomReservation studyRoomReservation: validateTime){
//            int startHour = studyRoomReservation.getReservationTime().getHour();
//            for(int i=0;i<studyRoomReservation.getUseHour();i++){
//                useHour.add(startHour+i);
//            }
//        }
//
//        boolean check_available=true;
//        int reservationStartHor = Integer.parseInt(studyRoomBookFormDto.getTime().split(":")[0]);
//        for(int i=0;i<studyRoomBookFormDto.getUse_hour();i++){
//            if(useHour.contains(reservationStartHor+i)){
//                check_available=false;
//                break;
//            }
//        }
//
//        if(check_available==false){
//            throw new RoomReservationOverlapException("이미 다른 사용자가 예약한 시간입니다.");
//        }

        StudyRoomReservation studyRoomReservation = StudyRoomReservation.builder()
                .room(studyRoom)
                .member(member)
                .useHour(studyRoomBookFormDto.getUse_hour())
                .personCnt(studyRoomBookFormDto.getPerson_cnt())
                .reservationTime(localDateTime)
                .state(ReservationState.WAIT)
                .build();
        studyRoomReservationRepository.save(studyRoomReservation);
        return studyRoomReservation.getId();
    }

    // 스터디룸 예약 취소
    public void studyRoomStateSetCancel(Long id) throws Exception {
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();
        // 예약날짜와 현재날짜가 같을 경우
        if(ChronoLocalDate.from(studyRoomReservation.getReservationTime()).isEqual(LocalDate.now())){
            throw new Exception("당일 예약 취소는 불가능합니다.");
        }
        studyRoomReservation.setState(ReservationState.CANCEL);
    }
}
