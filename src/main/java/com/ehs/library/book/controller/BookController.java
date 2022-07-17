package com.ehs.library.book.controller;

import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.service.BookHopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

//@Controller
//@RequestMapping(value = "/book")
//@RequiredArgsConstructor
public class BookController {
//    private final BookHopeService bookHopeService;
//
//    @GetMapping("hope/register")
//    public String registerHopeBookForm(Model model){
//        model.addAttribute("bookHopeFormDto", new BookHopeFormDto());
//        return "book/addHopeBookForm";
//    }
//
//    @PostMapping("hope/register")
//    public String registerHopeBook(@Valid BookHopeFormDto bookHopeFormDto, BindingResult bindingResult,
//                                   Model model, Principal principal){
//        if(bindingResult.hasErrors()){
//            return "book/addHopeBookForm";
//        }
//
//        try {
//            String email = principal.getName();
//            bookHopeService.registerBookHope(bookHopeFormDto, email);
//        } catch (Exception e){
//            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
//            return "book/addHopeBookForm";
//        }
//
//        return "redirect:/member/mypage";
//    }
}
