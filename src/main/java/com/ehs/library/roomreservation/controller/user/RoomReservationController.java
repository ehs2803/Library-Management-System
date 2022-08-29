package com.ehs.library.roomreservation.controller.user;

import com.ehs.library.roomreservation.service.user.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller("user.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @GetMapping("/reservation/studyroom")
    public String insertBookReservation(Principal principal, Model model){


        return "reservation/studyroom/studyroomList";
    }
}
