package com.ehs.library.roomreservation.controller.user;

import com.ehs.library.roomreservation.entity.StudyRoom;
import com.ehs.library.roomreservation.service.user.RoomReservationService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller("user.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @GetMapping("/reservation/studyroom")
    public String insertBookReservation(Model model){
        List<StudyRoom> studyRoomList = roomReservationService.getStudyRoom();

        model.addAttribute("studyRoomList", studyRoomList);

        return "reservation/studyroom/user/studyroomList";
    }
}
