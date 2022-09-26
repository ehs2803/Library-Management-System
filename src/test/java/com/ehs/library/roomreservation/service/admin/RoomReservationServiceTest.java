package com.ehs.library.roomreservation.service.admin;

import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.repository.MemberRepository;
import com.ehs.library.roomreservation.constant.ReservationState;
import com.ehs.library.roomreservation.constant.StudyRoomState;
import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.exception.RoomNameAlreadyExistException;
import com.ehs.library.roomreservation.repository.StudyRoomRepository;
import com.ehs.library.roomreservation.repository.StudyRoomReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class RoomReservationServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private StudyRoomRepository studyRoomRepository;
    @Autowired private StudyRoomReservationRepository studyRoomReservationRepository;

    @Autowired private RoomReservationService roomReservationService;
    @Autowired private com.ehs.library.roomreservation.service.user.RoomReservationService roomReservationServiceUser;

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
        memberRepository.save(saveMember1);
        memberRepository.save(saveMember2);
    }

    @Test
    @DisplayName("스터디룸 등록 - 성공")
    void registerStudyRoom() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(10)
                .location("location")
                .build();

        // when
        StudyRoom savedStudyRoom = roomReservationService.registerStudyRoom(studyRoomFormDto);
        StudyRoom findStudyRoom = studyRoomRepository.findById(savedStudyRoom.getId()).get();

        // then
        assertEquals("name1",findStudyRoom.getName());
        assertEquals("location",findStudyRoom.getLocation());
        assertEquals(10, findStudyRoom.getCapacity());
    }

    @Test
    @DisplayName("스터디룸 등록 - 이름 중복 예외")
    void registerStudyRoomException() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(10)
                .location("location")
                .build();

        StudyRoomFormDto studyRoomFormDto2 = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(10)
                .location("location")
                .build();

        // when
        StudyRoom savedStudyRoom = roomReservationService.registerStudyRoom(studyRoomFormDto);

        // then
        Throwable e = assertThrows(RoomNameAlreadyExistException.class, () ->
            roomReservationService.registerStudyRoom(studyRoomFormDto2));
        assertEquals("존재하는 스터디룸 이름입니다.",e.getMessage());

    }

    @Test
    @DisplayName("스터디룸 수정 성공")
    void EditStudyRoom() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(1)
                .location("location1")
                .build();

        StudyRoomFormDto studyRoomFormDto2 = StudyRoomFormDto.builder()
                .name("name2")
                .capacity(2)
                .location("location2")
                .build();
        StudyRoom savedStudyRoom1 = roomReservationService.registerStudyRoom(studyRoomFormDto);
        StudyRoom savedStudyRoom2 = roomReservationService.registerStudyRoom(studyRoomFormDto2);

        // when
        StudyRoomFormDto studyRoomEditDto = StudyRoomFormDto.builder()
                .id(savedStudyRoom1.getId())
                .name("name1")
                .capacity(10)
                .location("location10")
                .build();
        StudyRoomFormDto studyRoomEditDto2 = StudyRoomFormDto.builder()
                .id(savedStudyRoom2.getId())
                .name("name3")
                .capacity(20)
                .location("location20")
                .build();

        StudyRoom updateStudyRoom1 = roomReservationService.editStudyRoom(studyRoomEditDto);
        StudyRoom updateStudyRoom2 = roomReservationService.editStudyRoom(studyRoomEditDto2);

        // then
        assertEquals("name1", updateStudyRoom1.getName());
        assertEquals(10, updateStudyRoom1.getCapacity());
        assertEquals("location10", updateStudyRoom1.getLocation());
        assertEquals("name3", updateStudyRoom2.getName());
        assertEquals(20, updateStudyRoom2.getCapacity());
        assertEquals("location20", updateStudyRoom2.getLocation());
    }

    @Test
    @DisplayName("스터디룸 수정 예외 - 이름 중복")
    void EditStudyRoomException() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name1")
                .capacity(1)
                .location("location1")
                .build();

        StudyRoomFormDto studyRoomFormDto2 = StudyRoomFormDto.builder()
                .name("name2")
                .capacity(2)
                .location("location2")
                .build();
        StudyRoom savedStudyRoom1 = roomReservationService.registerStudyRoom(studyRoomFormDto);
        StudyRoom savedStudyRoom2 = roomReservationService.registerStudyRoom(studyRoomFormDto2);

        // when
        StudyRoomFormDto studyRoomEditDto = StudyRoomFormDto.builder()
                .id(savedStudyRoom1.getId())
                .name("name2")
                .capacity(10)
                .location("location10")
                .build();

        Throwable e = assertThrows(RoomNameAlreadyExistException.class, () ->
                roomReservationService.editStudyRoom(studyRoomEditDto));
        // then
        assertEquals("존재하는 스터디룸 이름입니다.",e.getMessage());
    }

    @Test
    @DisplayName("스터디룸 예약건 상태 사용중으로 변경 - 성공")
    void studyRoomStateSetUse() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationService.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();

        // when
        Long id = roomReservationServiceUser.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);
        roomReservationService.studyRoomStateSetUse(id);

        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findByIdFetchJoinRoom(id);

        // then
        assertEquals(ReservationState.USE, studyRoomReservation.getState());
        assertEquals(StudyRoomState.USE, studyRoomReservation.getRoom().getState());
    }

    @Test
    @DisplayName("스터디룸 예약건 상태 노쇼로 변경")
    void studyRoomStateSetNoShow() {
        // given
        StudyRoomFormDto studyRoomFormDto = StudyRoomFormDto.builder()
                .name("name3")
                .capacity(30)
                .location("location3")
                .build();
        StudyRoom studyRoom = roomReservationService.registerStudyRoom(studyRoomFormDto);

        StudyRoomBookFormDto studyRoomBookFormDto = StudyRoomBookFormDto.builder()
                .id(studyRoom.getId())
                .bookLocalDate("2022-09-01")
                .time("10:00")
                .useHour(2)
                .personCnt(1)
                .build();

        // when
        Long id = roomReservationServiceUser.reservationStudyRoom("test2@naver.com", studyRoomBookFormDto);
        roomReservationService.studyRoomStateSetNoShow(id, "test2@naver.com");

        StudyRoomReservation studyRoomReservation = studyRoomReservationRepository.findByIdFetchJoinRoom(id);
        Member member = memberRepository.findByEmail("test2@naver.com");
        // then
        assertEquals(ReservationState.NOSHOW, studyRoomReservation.getState());
        assertEquals(7,member.getSanctionStudyRoomDay());
    }

}