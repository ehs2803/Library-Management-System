package com.ehs.library.bookinterest.controller;

import com.ehs.library.bookinterest.service.BookInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BookInterestController {
    private final BookInterestService bookInterestService;

    // 관심도서에 추가
    @GetMapping("/book/interest/{id}")
    public String insertBookInterest(@PathVariable Long id, Principal principal){
        bookInterestService.insertBookInterest(principal.getName(), id);

        return "book/searchBookList";
    }

    // 관심도서에서 삭제하기
    @GetMapping("/book/interest/delete/{id}")
    public String deleteBookInterest(@PathVariable Long id){
        bookInterestService.deleteBookInterest(id);

        return "redirect:/member/mypage/interest";
    }
}
