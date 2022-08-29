package com.ehs.library.roomreservation.controller.admin;

import com.ehs.library.roomreservation.service.admin.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("admin.RoomReservationController")
@RequiredArgsConstructor
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @GetMapping("/admin/reservation/studyroom")
    public String adminPageStudyRoom(){


        return "/reservation/studyroom/admin/dashboardStudyRoom";
    }
}
