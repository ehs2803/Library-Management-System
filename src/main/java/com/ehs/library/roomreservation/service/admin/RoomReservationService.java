package com.ehs.library.roomreservation.service.admin;

import com.ehs.library.base.constant.Policy;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.exception.RoomNameAlreadyExistException;
import com.ehs.library.roomreservation.exception.RoomReservationOverAllowTimeException;
import com.ehs.library.roomreservation.exception.RoomReservationUseException;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import com.ehs.library.roomreservation.repository.StudyRoomReservationRepository;
import com.ehs.library.sanction.constant.SanctionState;
import com.ehs.library.sanction.constant.SanctionType;
import com.ehs.library.sanction.entity.Sanction;
import com.ehs.library.sanction.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service("admin.RoomReservationService")
@Transactional
@RequiredArgsConstructor
public class RoomReservationService {
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomReservationRepository studyRoomReservationRepository;
    private final SanctionRepository sanctionRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public StudyRoom findStudyRoomById(Long id){
        return studyRoomRepository.findById(id).get();
    }

    // 스터디룸 id 예약건수
    @Transactional(readOnly = true)
    public int countByStudyRoomReservation(Long id){
        return studyRoomReservationRepository.countByRoom(studyRoomRepository.findById(id).get());
    }

    // db에 등록된 스터디룸 조회
    @Transactional(readOnly = true)
    public List<StudyRoom> getStudyRoom(){
        return studyRoomRepository.findAll();
    }

    // 스터디룸 id과 관련된 예약(+멤버) 조회
    @Transactional(readOnly = true)
    public StudyRoom getStudyRoomFetchJoinAll(Long id){
        return studyRoomRepository.findByIdFetchJoinAll(id);
    }

    // 스터디룸 예약 상태 WAIT 조회
    @Transactional(readOnly = true)
    public List<StudyRoomReservation> findByStateWaitFetchJoin(){
        return studyRoomReservationRepository.findByStateWaitFetchJoin();
    }

    // 스터디룸 예약 상태 ALLOW 조회
    @Transactional(readOnly = true)
    public List<StudyRoomReservation> findByStateAllowFetchJoin(){
        return studyRoomReservationRepository.findByStateAllowFetchJoin();
    }

    // 스터디룸 예약 상태 COMPLETE, REJECT 조회
    @Transactional(readOnly = true)
    public List<StudyRoomReservation> findByStateCompleteAndRejectFetchJoin(){
        return studyRoomReservationRepository.findByStateCompleteAndRejectFetchJoin();
    }

    // 스터디룸 예약 상태 USE 조회
    @Transactional(readOnly = true)
    public List<StudyRoomReservation> findByStateUseFetchJoinRoom(){
        return studyRoomReservationRepository.findByStateUseFetchJoinRoom();
    }

    // 스터디룸 등록
    public StudyRoom registerStudyRoom(StudyRoomFormDto studyRoom){
        StudyRoom validStudyRoom = studyRoomRepository.findByName(studyRoom.getName());
        if(validStudyRoom != null){ // 스터디룸 이름이 이미 존재하는 경우
            throw new RoomNameAlreadyExistException("존재하는 스터디룸 이름입니다.");
        }
        System.out.println(11);
        StudyRoom registerStudyRoom = StudyRoom.builder()
                .name(studyRoom.getName())
                .location(studyRoom.getLocation())
                .capacity(studyRoom.getCapacity())
                .state(StudyRoomState.AVAILABLE)
                .build();

        return studyRoomRepository.save(registerStudyRoom);
    }

    // 스터디룸 상태 변경
    public Long changeStateStudyRoom(Long id){
        StudyRoom studyRoom = studyRoomRepository.findById(id).get();
        // AVAILABLE인경우 CLOSE로 변경
        if(studyRoom.getState().toString().equals("AVAILABLE")){
            studyRoom.setState(StudyRoomState.CLOSE);
        }
        else{ // CLOSE인경우 AVAILABLE로 변경
            studyRoom.setState(StudyRoomState.AVAILABLE);
        }
        return studyRoom.getId();
    }

    // 스터디룸 수정을 위한 DTO 프로퍼티 설정 및 반환
    @Transactional(readOnly = true)
    public StudyRoomFormDto getStudyRoomEditFormDto(Long id){
        StudyRoom studyRoom = studyRoomRepository.findById(id).get();
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .id(studyRoom.getId())
                .name(studyRoom.getName())
                .location(studyRoom.getLocation())
                .capacity(studyRoom.getCapacity())
                .build();
        return studyRoomFormDto;
    }

    // 스터디룸 수정
    public StudyRoom editStudyRoom(StudyRoomFormDto studyRoomFormDto){
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomFormDto.getId()).get();
        if(!studyRoom.getName().equals(studyRoomFormDto.getName())){
            StudyRoom validStudyRoom = studyRoomRepository.findByName(studyRoomFormDto.getName());
            if(validStudyRoom != null){ // 스터디룸 이름이 이미 존재하는 경우
                throw new RoomNameAlreadyExistException("존재하는 스터디룸 이름입니다.");
            }
        }

        studyRoom.setName(studyRoomFormDto.getName());
        studyRoom.setLocation(studyRoomFormDto.getLocation());
        studyRoom.setCapacity(studyRoomFormDto.getCapacity());
        return studyRoom;
    }

    // 스터디룸 예약 상태 ALLOW로 변경
    public void studyRoomStateSetAllow(Long id) {
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();

        LocalDateTime now = LocalDateTime.now();

        // 예약승인 시간(현재시간)이 예약시간보다 늦은 경우
        if(now.isAfter(studyRoomReservation.getReservationTime())){
            studyRoomReservation.setState(ReservationState.REJECT);
            throw new RoomReservationOverAllowTimeException("예약한 시간을 이미 지났습니다. 해당 예약은 거절처리 됩니다.");
        }
        studyRoomReservation.setState(ReservationState.ALLOW);
    }

    // 스터디룸 예약 상태 REJECT로 변경
    public void studyRoomStateSetReject(Long id){
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();
        studyRoomReservation.setState(ReservationState.REJECT);
    }

    // 스터디룸 예약 상태 USE로 변경
    public void studyRoomStateSetUse(Long id) {
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findByIdFetchJoinRoom(id);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime enterAvailable = studyRoomReservation.getReservationTime().minus(10, ChronoUnit.MINUTES);

        // 입실처리하려는 시간이(현재시간) 예약시간 10분전 이상일경우... 예약시간 10분전부터 입실처리 가능.
        if(now.isBefore(enterAvailable)){
           String errorMsg = studyRoomReservation.getCreatedBy()+"가 예약한 "+studyRoomReservation.getRoom().getName()+
                    "예약은 예약시간인 "+studyRoomReservation.getReservationTime()+" 10분전부터 입실처리가 가능합니다.";
            throw new RoomReservationUseException(errorMsg);
        }

        studyRoomReservation.setStartTime(LocalDateTime.now()); // 입실시간 설정
        studyRoomReservation.setState(ReservationState.USE);
        StudyRoom studyRoom = studyRoomReservation.getRoom();
        studyRoom.setState(StudyRoomState.USE); // 해당 스터디룸 USE로 상태 변경
    }

    // 스터디룸 예약 상태 NOSHOW로 변경
    public void studyRoomStateSetNoShow(Long id, String email){
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();
        studyRoomReservation.setState(ReservationState.NOSHOW);
        Member member = memberRepository.findByEmail(email);
        Sanction sanction = Sanction.builder()
                .member(member)
                .sanctionDay(Policy.SANCTION_DAY_STUDYROOM)
                .type(SanctionType.STUDYROOM)
                .studyRoomReservation(studyRoomReservation)
                .build();
        sanctionRepository.save(sanction);
        member.setSanctionStudyRoomDay(member.getSanctionStudyRoomDay()+Policy.SANCTION_DAY_STUDYROOM);
    }

    // 스터디룸 예약 상태 COMPLETE로 변경
    public void studyRoomStateSetComplete(Long id){
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findByIdFetchJoinRoom(id);
        studyRoomReservation.setState(ReservationState.COMPLETE);
        studyRoomReservation.setEndTime(LocalDateTime.now()); // 퇴실 시간 설정
        StudyRoom studyRoom = studyRoomReservation.getRoom();
        studyRoom.setState(StudyRoomState.AVAILABLE); // 해당 스터디룸 상태 AVAILABLE로 설정
    }
}
