package com.ehs.library.roomreservation.controller.admin;

import com.ehs.library.book.dto.BookListDto;
import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.dto.StudyRoomDto;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomListDto;
import com.ehs.library.roomreservation.dto.StudyRoomReservationDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.exception.RoomNameAlreadyExistException;
import com.ehs.library.roomreservation.exception.RoomReservationOverAllowTimeException;
import com.ehs.library.roomreservation.exception.RoomReservationUseException;
import com.ehs.library.roomreservation.service.admin.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("admin.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;
    private final ModelMapper modelMapper;

    // 관리자페이지 스터디룸 관리 데시보드 페이지
    @GetMapping("/admin/reservation/studyroom")
    public String adminPageStudyRoom(){
        return "reservation/studyroom/admin/dashboardStudyRoom";
    }

    // 스터디룸 관리 스터디룸 리스트
   @GetMapping("/admin/studyroom/list")
    public String adminPageStudyRoomList(Model model){
        List<StudyRoom> studyRoomList_entity = roomReservationService.getStudyRoom();

       // ModelMapper이용해 List<Entity> -> List<Dto>
       List<StudyRoomListDto> studyRoomList = studyRoomList_entity.stream()
               .map(studyroom->modelMapper.map(studyroom, StudyRoomListDto.class))
               .collect(Collectors.toList());

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/admin/studyRoomList";
    }

    // 스터디룸 등록 폼
    @GetMapping("/admin/studyroom/add")
    public String adminPageStudyRoomRegister(Model model){
        model.addAttribute("studyRoomAddFormDto", new StudyRoomFormDto());

        return "reservation/studyroom/admin/studyRoomRegisterForm";
    }

    // 스터디룸 등록
    @PostMapping("/admin/studyroom/add")
    public String registerStudyRoom(@Valid StudyRoomFormDto studyRoomFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        try {
            roomReservationService.registerStudyRoom(studyRoomFormDto);
        } catch (RoomNameAlreadyExistException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        return "redirect:/admin/studyroom/list";
    }

    // 스터디룸 상태 변경 (예약가능<-->예약불가능)
    @GetMapping("/admin/studyroom/state/{id}")
    public String changeStateStudyRoom(@PathVariable Long id){
        roomReservationService.changeStateStudyRoom(id);

        return "redirect:/admin/studyroom/list";
    }

    // 스터디룸 수정 폼
    @GetMapping("/admin/studyroom/edit/{id}")
    public String editStudyRoomForm(@PathVariable Long id, Model model){
        StudyRoomFormDto studyRoomFormDto = roomReservationService.getStudyRoomEditFormDto(id);
        model.addAttribute("studyRoomFormDto", studyRoomFormDto);

        return "reservation/studyroom/admin/studyRoomEditForm";
    }

    // 스터디룸 수정
    @PostMapping("/admin/studyroom/edit")
    public String editStudyRoom(@Valid StudyRoomFormDto studyRoomFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/admin/studyRoomEditForm";
        }

        try {
            roomReservationService.editStudyRoom(studyRoomFormDto);
        } catch (RoomNameAlreadyExistException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/admin/studyRoomEditForm";
        }

        return "redirect:/admin/studyroom/list";
    }

    // 스터디룸 id 정보 및 예약 기록 조회 ** NPE 문제 있음.. 임시적으로 해결함. 추후 고칠예정..
    @GetMapping("/admin/reservation/studyroom/book/{id}")
    public String getStudyRoomReservationList(@PathVariable Long id, Model model){
        StudyRoom studyRoom_entity_check = roomReservationService.getStudyRoomFetchJoinAll(id);
        StudyRoom studyRoom_entity;
        List<StudyRoomReservation> studyRoomReservationList_entity;
        StudyRoomDto studyRoom;
        if(studyRoom_entity_check==null){
            studyRoom_entity = roomReservationService.findStudyRoomById(id);
            studyRoom = StudyRoomDto.builder()
                    .name(studyRoom_entity.getName())
                    .location(studyRoom_entity.getLocation())
                    .capacity(studyRoom_entity.getCapacity())
                    .build();
            studyRoomReservationList_entity=new ArrayList<>();
        }
        else{
            studyRoomReservationList_entity = studyRoom_entity_check.getReservations();
            studyRoom = StudyRoomDto.builder()
                    .name(studyRoom_entity_check.getName())
                    .location(studyRoom_entity_check.getLocation())
                    .capacity(studyRoom_entity_check.getCapacity())
                    .build();
        }


//        studyRoom = StudyRoomDto.builder()
//                .name(studyRoom_entity.getName())
//                .location(studyRoom_entity.getLocation())
//                .capacity(studyRoom_entity.getCapacity())
//                .build();
        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoom", studyRoom);
        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationListAll";
    }

    // 스터디룸 예약 승인 대기 목록 조회
    @GetMapping("/admin/reservation/studyroom/wait")
    public String studyRoomReservationWaitList(Model model){
        List<StudyRoomReservation> studyRoomReservationList_entity = roomReservationService.findByStateWaitFetchJoin();

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationWaitList";
    }

    // 스터디룸 예약 승인 완료 목록 조회
    @GetMapping("/admin/reservation/studyroom/allow")
    public String studyRoomReservationAllowList(Model model){
        List<StudyRoomReservation> studyRoomReservationList_entity = roomReservationService.findByStateAllowFetchJoin();

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationAllowList";
    }

    // 스터디룸 예약 처리 완료 목록 조회
    @GetMapping("/admin/reservation/studyroom/complete")
    public String studyRoomReservationCompleteList(Model model){
        List<StudyRoomReservation> studyRoomReservationList_entity = roomReservationService.findByStateCompleteAndRejectFetchJoin();

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationCompleteList";
    }

    // 스터디룸 사용중 목록 조회
    @GetMapping("/admin/reservation/studyroom/use")
    public String studyRoomUse(Model model){
        List<StudyRoomReservation> studyRoomReservationList_entity = roomReservationService.findByStateUseFetchJoinRoom();

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("studyRoomReservationList",studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationUseList";
    }

    // 스터디룸 예약 상태 ALLOW로 설정 (예약승인)
    @GetMapping("/admin/reservation/studyroom/allow/{id}")
    public String studyRoomReservationAllow(@PathVariable Long id, Model model){
        try{
            roomReservationService.studyRoomStateSetAllow(id);
        } catch (RoomReservationOverAllowTimeException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/reservation/studyroom/complete";
        }

        return "redirect:/admin/reservation/studyroom/allow";
    }

    // 스터디룸 예약 상태 REJECT로 설정 (예약거절)
    @GetMapping("/admin/reservation/studyroom/reject/{id}")
    public String studyRoomReservationReject(@PathVariable Long id){
        roomReservationService.studyRoomStateSetReject(id);

        return "redirect:/admin/reservation/studyroom/complete";
    }

    // 스터디룸 예약 상태 USE로 설정 (입실)
    @GetMapping("/admin/reservation/studyroom/use/{id}")
    public String studyRoomUse(@PathVariable Long id, Model model){
        try{
            roomReservationService.studyRoomStateSetUse(id);
        } catch (RoomReservationUseException e){
            List<StudyRoomReservation> studyRoomReservationList_entity = roomReservationService.findByStateAllowFetchJoin();

            ModelMapper modelMapper = new ModelMapper(); // ModelMapper이용해 List<Entity> -> List<Dto>
            List<StudyRoomReservationDto> studyRoomReservationList = studyRoomReservationList_entity.stream()
                    .map(reservation->modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());

            model.addAttribute("studyRoomReservationList", studyRoomReservationList);
            model.addAttribute("errorMessage", e.getMessage());

            return "reservation/studyroom/admin/studyRoomReservationAllowList";
        }

        return "redirect:/admin/reservation/studyroom/use";
    }

    // 스터디룸 예약 상태 NOSHOW로 설정
    @GetMapping("/admin/reservation/studyroom/noshow/{id}")
    public String studyRoomNoShow(@PathVariable Long id, Principal principal){
        roomReservationService.studyRoomStateSetNoShow(id, principal.getName());

        return "redirect:/admin/reservation/studyroom/complete";
    }

    // 스터디룸 예약 상태 COMPLETE로 설정 (퇴실)
    @GetMapping("/admin/reservation/studyroom/complete/{id}")
    public String studyRoomComplete(@PathVariable Long id){
        roomReservationService.studyRoomStateSetComplete(id);

        return "redirect:/admin/reservation/studyroom/complete";
    }
}
