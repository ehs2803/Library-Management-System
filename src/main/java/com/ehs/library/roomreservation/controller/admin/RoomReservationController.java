package com.ehs.library.roomreservation.controller.admin;

import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.dto.StudyRoomAddFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.service.admin.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller("admin.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @GetMapping("/admin/reservation/studyroom")
    public String adminPageStudyRoom(){

        return "reservation/studyroom/admin/dashboardStudyRoom";
    }

    @GetMapping("/admin/reservation/studyroom/list")
    public String adminPageStudyRoomList(Model model){
        List<StudyRoom> studyRoomList = roomReservationService.getStudyRoom();

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/admin/studyRoomList";
    }

    @GetMapping("/admin/reservation/studyroom/add")
    public String adminPageStudyRoomRegister(Model model){
        model.addAttribute("studyRoomAddFormDto", new StudyRoomAddFormDto());

        return "reservation/studyroom/admin/studyRoomRegisterForm";
    }

    @PostMapping("/admin/reservation/studyroom/add")
    public String registerStudyRoom(@Valid StudyRoomAddFormDto studyRoomAddFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        try {
            roomReservationService.registerStudyRoom(studyRoomAddFormDto);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        return "redirect:/admin/reservation/studyroom/list";
    }
}
