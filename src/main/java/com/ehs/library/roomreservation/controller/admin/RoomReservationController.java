package com.ehs.library.roomreservation.controller.admin;

import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.service.admin.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("studyRoomAddFormDto", new StudyRoomFormDto());

        return "reservation/studyroom/admin/studyRoomRegisterForm";
    }

    @PostMapping("/admin/reservation/studyroom/add")
    public String registerStudyRoom(@Valid StudyRoomFormDto studyRoomFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        try {
            roomReservationService.registerStudyRoom(studyRoomFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/admin/studyRoomRegisterForm";
        }

        return "redirect:/admin/reservation/studyroom/list";
    }

    @GetMapping("/admin/reservation/studyroom/state/{id}")
    public String changeStateStudyRoom(@PathVariable Long id){
        roomReservationService.changeStateStudyRoom(id);

        return "redirect:/admin/reservation/studyroom/list";
    }

    @GetMapping("/admin/reservation/studyroom/edit/{id}")
    public String editStudyRoomForm(@PathVariable Long id, Model model){
        StudyRoomFormDto studyRoomFormDto = roomReservationService.getStudyRoom(id);
        model.addAttribute("studyRoomFormDto", studyRoomFormDto);

        return "reservation/studyroom/admin/studyRoomEditForm";
    }

    @PostMapping("/admin/reservation/studyroom/edit")
    public String editStudyRoom(@Valid StudyRoomFormDto studyRoomFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reservation/studyroom/admin/studyRoomEditForm";
        }

        try {
            roomReservationService.editStudyRoom(studyRoomFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation/studyroom/admin/studyRoomEditForm";
        }

        return "redirect:/admin/reservation/studyroom/list";
    }
}
