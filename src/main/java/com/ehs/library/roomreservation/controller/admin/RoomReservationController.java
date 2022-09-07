package com.ehs.library.roomreservation.controller.admin;

import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
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

    @GetMapping("/admin/studyroom/list")
    public String adminPageStudyRoomList(Model model){
        List<StudyRoom> studyRoomList = roomReservationService.getStudyRoom();

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/admin/studyRoomList";
    }

    @GetMapping("/admin/studyroom/add")
    public String adminPageStudyRoomRegister(Model model){
        model.addAttribute("studyRoomAddFormDto", new StudyRoomFormDto());

        return "reservation/studyroom/admin/studyRoomRegisterForm";
    }

    @PostMapping("/admin/studyroom/add")
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

        return "redirect:/admin/studyroom/list";
    }

    @GetMapping("/admin/studyroom/state/{id}")
    public String changeStateStudyRoom(@PathVariable Long id){
        roomReservationService.changeStateStudyRoom(id);

        return "redirect:/admin/studyroom/list";
    }

    @GetMapping("/admin/studyroom/edit/{id}")
    public String editStudyRoomForm(@PathVariable Long id, Model model){
        StudyRoomFormDto studyRoomFormDto = roomReservationService.getStudyRoom(id);
        model.addAttribute("studyRoomFormDto", studyRoomFormDto);

        return "reservation/studyroom/admin/studyRoomEditForm";
    }

    @PostMapping("/admin/studyroom/edit")
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

        return "redirect:/admin/studyroom/list";
    }

    @GetMapping("/admin/reservation/studyroom/book/{id}")
    public String getStudyRoomReservationList(@PathVariable Long id, Model model){
        StudyRoom studyRoom = roomReservationService.getStudyRoomFetchJoinAll(id);
        List<StudyRoomReservation> studyRoomReservationList = studyRoom.getReservations();

        model.addAttribute("studyRoom", studyRoom);
        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationListAll";
    }

    @GetMapping("/admin/reservation/studyroom/wait")
    public String studyRoomReservationWaitList(Model model){
        List<StudyRoomReservation> studyRoomReservationList = roomReservationService.findByStateWaitFetchJoin();

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationWaitList";
    }

    @GetMapping("/admin/reservation/studyroom/allow")
    public String studyRoomReservationAllowList(Model model){
        List<StudyRoomReservation> studyRoomReservationList = roomReservationService.findByStateAllowFetchJoin();

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationAllowList";
    }

    @GetMapping("/admin/reservation/studyroom/complete")
    public String studyRoomReservationCompleteList(Model model){
        List<StudyRoomReservation> studyRoomReservationList = roomReservationService.findByStateCompleteAndRejectFetchJoin();

        model.addAttribute("studyRoomReservationList", studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationCompleteList";
    }

    @GetMapping("/admin/reservation/studyroom/allow/{id}")
    public String studyRoomReservationAllow(@PathVariable Long id){
        roomReservationService.studyRoomStateSetAllow(id);

        return "redirect:/admin/reservation/studyroom/allow";
    }

    @GetMapping("/admin/reservation/studyroom/reject/{id}")
    public String studyRoomReservationReject(@PathVariable Long id){
        roomReservationService.studyRoomStateSetReject(id);

        return "redirect:/admin/reservation/studyroom/complete";
    }

    @GetMapping("/admin/reservation/studyroom/use/{id}")
    public String studyRoomUse(@PathVariable Long id){
        roomReservationService.studyRoomStateSetUse(id);

        return "redirect:/admin/reservation/studyroom/use";
    }

    @GetMapping("/admin/reservation/studyroom/noshow/{id}")
    public String studyRoomNoShow(@PathVariable Long id){
        roomReservationService.studyRoomStateSetNoShow(id);

        return "redirect:/admin/reservation/studyroom/complete";
    }

    @GetMapping("/admin/reservation/studyroom/use")
    public String studyRoomUse(Model model){
        List<StudyRoomReservation> studyRoomReservationList = roomReservationService.findByStateUseFetchJoinRoom();

        model.addAttribute("studyRoomReservationList",studyRoomReservationList);

        return "reservation/studyroom/admin/studyRoomReservationUseList";
    }

    @GetMapping("/admin/reservation/studyroom/complete/{id}")
    public String studyRoomComplete(@PathVariable Long id){
        roomReservationService.studyRoomStateSetComplete(id);

        return "redirect:/admin/reservation/studyroom/complete";
    }
}
