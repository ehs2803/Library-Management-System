package com.ehs.library.bookreservation.controller;

import com.ehs.library.bookreservation.service.BookReservationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BookReservationController {
    private final BookReservationService bookReservationService;

    @GetMapping("/book/reservation/{id}")
    public String insertBookReservation(@PathVariable Long id, Principal principal){
        bookReservationService.reservationBook(principal.getName(), id);

        return "book/searchBookList";
    }

    @GetMapping("/book/reservation/delete/{id}")
    public String deleteBookReservation(@PathVariable Long id){
        bookReservationService.deleteReservation(id);

        return "redirect:/member/mypage/reservation";
    }
}
