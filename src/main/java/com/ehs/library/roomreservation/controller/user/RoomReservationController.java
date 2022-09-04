package com.ehs.library.roomreservation.controller.user;

import com.ehs.library.roomreservation.dto.StudyRoomBookFormDto;
import com.ehs.library.roomreservation.dto.StudyRoomFormDto;
import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.service.user.RoomReservationService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
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

@Controller("user.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @GetMapping("/reservation/studyroom")
    public String insertBookReservation(Model model){
        List<StudyRoom> studyRoomList = roomReservationService.getStudyRoom();

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/user/studyRoomList";
    }

    @GetMapping("/reservation/studyroom/book/{id}")
    public String reservationStudyRoomForm(@PathVariable Long id, Model model){
        StudyRoom studyRoom = roomReservationService.getStudyRoomFetchJoin(id);
        List<StudyRoomReservation> studyRoomReservationList;

        if(studyRoom==null){
            studyRoom = roomReservationService.findById(id);
            studyRoomReservationList = new ArrayList<>();
        }
        else {
            studyRoomReservationList = studyRoom.getReservations();
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
        model.addAttribute("minDate", startDate);
        model.addAttribute("maxDate", endDate);
        model.addAttribute("minTime","9:00:00");
        model.addAttribute("maxTime","20:00:00");
        model.addAttribute("maxCapacity", studyRoom.getCapacity());

        return "reservation/studyroom/user/studyRoomBookForm";
    }

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

    @GetMapping("/reservation/studyroom/{id}")
    public String getStudyRoomReservationList(@PathVariable Long id, Model model){
        StudyRoom studyRoom = roomReservationService.getStudyRoomFetchJoin(id);
        List<StudyRoomReservation> studyRoomReservationList;

        if(studyRoom==null){
            studyRoom = roomReservationService.findById(id);
            studyRoomReservationList = new ArrayList<>();
        }
        else {
            studyRoomReservationList = studyRoom.getReservations();
        }

        model.addAttribute("studyRoom", studyRoom);
        model.addAttribute("studyRoomReservationList",studyRoomReservationList);

        return "reservation/studyroom/user/studyRoomReservationList";
    }
}
