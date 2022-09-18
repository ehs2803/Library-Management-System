package com.ehs.library.bookreservation.controller;

import com.ehs.library.bookreservation.service.BookReservationService;
import com.ehs.library.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BookReservationController {
    private final BookReservationService bookReservationService;

    // 도서 예약하기
    @GetMapping("/book/reservation/{id}")
    public String insertBookReservation(@PathVariable Long id, Principal principal, Model model){
        try {
            bookReservationService.reservationBook(principal.getName(), id);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "book/searchBookList";
        }

        return "book/searchBookList";
    }

    // 도서 예약 취소하기
    @GetMapping("/book/reservation/delete/{id}")
    public String deleteBookReservation(@PathVariable Long id){
        bookReservationService.deleteReservation(id);

        return "redirect:/member/mypage/reservation";
    }
}
