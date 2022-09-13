package com.ehs.library.roomreservation.controller.user;

import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomListDto;
import com.ehs.library.roomreservation.dto.StudyRoomReservationDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.service.user.RoomReservationService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("user.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    // 스터디룸 목록 조회
    @GetMapping("/reservation/studyroom")
    public String insertBookReservation(Model model){
        List<StudyRoom> studyRoomList_entity = roomReservationService.getStudyRoom();

        ModelMapper modelMapper = new ModelMapper(); // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomListDto> studyRoomList = studyRoomList_entity.stream()
                .map(studyRoom->modelMapper.map(studyRoom, StudyRoomListDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/user/studyRoomList";
    }

    // 특정 스터디룸 예약 폼
    @GetMapping("/reservation/studyroom/book/{id}")
    public String reservationStudyRoomForm(@PathVariable Long id, Model model){
        StudyRoom studyRoom_entity = roomReservationService.findByIdAndStateWaitOrAllowFetchJoi(id);
        List<StudyRoomReservation> studyRoomReservationList_entity;

        StudyRoomListDto studyRoom;
        List<StudyRoomReservationDto> studyRoomReservationList;
        ModelMapper modelMapper = new ModelMapper(); // ModelMapper이용해 List<Entity> -> List<Dto>

        if(studyRoom_entity==null){ // state가 wait or allow인 스터디룸이 없는 경우
            studyRoom_entity = roomReservationService.findById(id);
            studyRoomReservationList_entity = new ArrayList<>();

            studyRoom = StudyRoomListDto.builder()
                    .name(studyRoom_entity.getName())
                    .location(studyRoom_entity.getLocation())
                    .capacity(studyRoom_entity.getCapacity())
                    .state(studyRoom_entity.getState().toString())
                    .build();
            studyRoomReservationList = studyRoomReservationList_entity.stream()
                    .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
        }
        else {
            studyRoomReservationList_entity = studyRoom_entity.getReservations();

            studyRoom = StudyRoomListDto.builder()
                    .name(studyRoom_entity.getName())
                    .location(studyRoom_entity.getLocation())
                    .capacity(studyRoom_entity.getCapacity())
                    .state(studyRoom_entity.getState().toString())
                    .build();
            studyRoomReservationList = studyRoomReservationList_entity.stream()
                    .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
        }

        StudyRoomBookFormDto studyRoomBookFormDto = new StudyRoomBookFormDto();
        studyRoomBookFormDto.setId(id);

        LocalDate nowDate = LocalDate.now();
        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(7);

        // studyroom id가 예약목록 반환
        model.addAttribute("studyRoom", studyRoom);
        model.addAttribute("studyRoomReservationList",studyRoomReservationList);
        model.addAttribute("bookForm", studyRoomBookFormDto);
        model.addAttribute("minDate", startDate); // 예약가능한 시작 날짜
        model.addAttribute("maxDate", endDate); // 예약가능한 마지막 날짜
        model.addAttribute("minTime","9:00:00"); // 예약가능한 시작 시간
        model.addAttribute("maxTime","20:00:00"); // 예약가능한 마지막 시간
        model.addAttribute("maxCapacity", studyRoom.getCapacity()); // 스터디룸 수용인원

        return "reservation/studyroom/user/studyRoomBookForm";
    }

    // 스터디룸 예약
    @PostMapping("/reservation/studyroom/book")
    public String reservationStudyRoom(@Valid StudyRoomBookFormDto studyRoomBookFormDto, BindingResult bindingResult,
                                       Principal principal,Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/user/studyRoomBookForm";
        }

        try {
            roomReservationService.reservationStudyRoom(principal.getName(), studyRoomBookFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/user/studyRoomBookForm";
        }

        return "redirect:/reservation/studyroom";
    }

    // 특정 스터디룸의 예약현황 조회
    @GetMapping("/reservation/studyroom/{id}")
    public String getStudyRoomReservationList(@PathVariable Long id, Model model){
        StudyRoom studyRoom_entity = roomReservationService.findByIdAndStateWaitOrAllowFetchJoi(id);
        List<StudyRoomReservation> studyRoomReservationList_entity;

        StudyRoomListDto studyRoom;
        List<StudyRoomReservationDto> studyRoomReservationList;
        ModelMapper modelMapper = new ModelMapper(); // ModelMapper이용해 List<Entity> -> List<Dto>

        if(studyRoom_entity==null){ // state가 wait or allow인 스터디룸이 없는 경우
            studyRoom_entity = roomReservationService.findById(id);
            studyRoomReservationList_entity = new ArrayList<>();

            studyRoom = StudyRoomListDto.builder()
                    .name(studyRoom_entity.getName())
                    .location(studyRoom_entity.getLocation())
                    .capacity(studyRoom_entity.getCapacity())
                    .state(studyRoom_entity.getState().toString())
                    .build();
            studyRoomReservationList = studyRoomReservationList_entity.stream()
                    .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
        }
        else {
            studyRoomReservationList_entity = studyRoom_entity.getReservations();

            studyRoom = StudyRoomListDto.builder()
                    .name(studyRoom_entity.getName())
                    .location(studyRoom_entity.getLocation())
                    .capacity(studyRoom_entity.getCapacity())
                    .state(studyRoom_entity.getState().toString())
                    .build();
            studyRoomReservationList = studyRoomReservationList_entity.stream()
                    .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
        }

        model.addAttribute("studyRoom", studyRoom);
        model.addAttribute("studyRoomReservationList",studyRoomReservationList);

        return "reservation/studyroom/user/studyRoomReservationList";
    }
}
