package com.ehs.library.roomreservation.service.user;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.exception.UserAlreadyExistException;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.exception.RoomReservationCancelException;
import com.ehs.library.roomreservation.exception.RoomReservationOverlapException;
import com.ehs.library.roomreservation.exception.RoomReservationSanctionException;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import com.ehs.library.roomreservation.repository.StudyRoomReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class RoomReservationServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private StudyRoomReservationRepository studyRoomReservationRepository;

    @Autowired private RoomReservationService roomReservationService;
    @Autowired private com.ehs.library.roomreservation.service.admin.RoomReservationService roomReservationServiceAdmin;
    @BeforeEach
    public void save(){
        Member saveMember1 = Member.builder()
                .name("test1")
                .email("test1@naver.com")
                .password("test123456")
                .address("tempAddress")
                .role(Role.ADMIN)
                .build();
        Member saveMember2 = Member.builder()
                .name("test2")
                .email("test2@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .build();
        Member saveMember3 = Member.builder()
                .name("test3")
                .email("test3@naver.com")
                .password("test123456")
                .address("tempAddress2")
                .role(Role.USER)
                .sanctionStudyRoomDay(7)
                .build();
        memberRepository.save(saveMember1);
        memberRepository.save(saveMember2);
        memberRepository.save(saveMember3);

        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(10)
                .location("location")
                .build();
        StudyRoomFormDto studyRoomFormDto2 = StudyRoomFormDto.builder()
                .name("name2")
                .capacity(10)
                .location("location")
                .build();
        roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);
        roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto2);
    }


    @Test
    @DisplayName("모든 스터디룸 조회")
    void getStudyRoom() {
        // when
        List<StudyRoom> studyRoomList = roomReservationService.getStudyRoom();

        // then
        assertEquals(2, studyRoomList.size());
        for(StudyRoom studyRoom : studyRoomList){
            System.out.println(studyRoom.getId());
            System.out.println(studyRoom.getName());
            System.out.println(studyRoom.getCapacity());
            System.out.println(studyRoom.getLocation());
            System.out.println("=============================");
        }
    }

    @Test
    @DisplayName("스터디룸 예약 성공")
    void reservationStudyRoom() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();

        // when
        Long id = roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);
        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();

        //then
        assertEquals(2,studyRoomReservation.getUseHour());
        assertEquals(1,studyRoomReservation.getPersonCnt());
        assertEquals(LocalDateTime.of(2022,9,1,10,0,0), studyRoomReservation.getReservationTime());
    }

    @Test
    @DisplayName("스터디룸 예약 실패 - 제재 예외")
    void reservationStudyRoomException1() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();

        // when
        Throwable e = assertThrows(RoomReservationSanctionException.class, () ->
            roomReservationService.reservationStudyRoom("test3@naver.com", studyRoomBookFormDto));

        // then
        assertEquals("현재 제재중입니다. 스터디룸 예약이 불가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("스터디룸 예약 실패 - 겹치는 시간 예외")
    void reservationStudyRoomException2() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();
        StudyRoomBookFormDto studyRoomBookFormDto2 = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("11:00")
                .useHour(1)
                .personCnt(1)
                .build();
        roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);

        // when
        Throwable e = assertThrows(RoomReservationOverlapException.class, () ->
                roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto));

        // then
        assertEquals("이미 다른 사용자가 예약한 시간입니다.", e.getMessage());
    }

    @Test
    @DisplayName("특정 멤버의 스터디룸 예약 조회")
    void findByMemberFetchJoinRoom() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto1 = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();
        StudyRoomBookFormDto studyRoomBookFormDto2 = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-20")
                .time("12:00")
                .useHour(2)
                .personCnt(1)
                .build();
        roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto1);
        roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto2);

        // when
        List<StudyRoomReservation> studyRoomReservationList =
                roomReservationService.findByMemberFetchJoinRoom("test2@naver.com");

        // then
        assertEquals(2, studyRoomReservationList.size());
        for(StudyRoomReservation studyRoomReservation : studyRoomReservationList){
            System.out.println(studyRoomReservation.getRoom().getName());
            System.out.println(studyRoomReservation.getReservationTime());
            System.out.println("==============================");
        }
    }


    @Test
    @DisplayName("스터디룸 예약 취소 성공")
    void studyRoomStateSetCancel() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();
        Long id = roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);

        // when
        roomReservationService.studyRoomStateSetCancel(id);

        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findById(id).get();

        // then
        assertEquals(ReservationState.CANCEL, studyRoomReservation.getState());
    }

    @Test
    @DisplayName("스터디룸 예약 취소 실패 - 당일 취소 예외")
    void studyRoomStateSetCancelException() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationServiceAdmin.registerStudyRoom(studyRoomFormDto);

        LocalDateTime now = LocalDateTime.now();
        String today=now.getYear()+"-";
        if(String.valueOf(now.getMonthValue()).length()==1){
            today+="0";
            today+=now.getMonthValue();
        }
        else today+=now.getMonthValue();
        today+="-";
        if(String.valueOf(now.getDayOfMonth()).length()==1){
            today+="0";
            today+=now.getDayOfMonth();
        }
        else today+=now.getDayOfMonth();
        System.out.println(today);
        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate(today)
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();
        Long id = roomReservationService.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);

        // when
        Throwable e = assertThrows(RoomReservationCancelException.class, () ->
                roomReservationService.studyRoomStateSetCancel(id));

        // then
        assertEquals("당일 예약 취소는 불가능합니다.", e.getMessage());
    }
}