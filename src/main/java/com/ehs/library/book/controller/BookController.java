package com.ehs.library.book.controller;

import com.ehs.library.book.service.BookService;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.service.BookHopeService;
import com.ehs.library.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = "/main/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/search")
    public String searchBookList(){


        return "book/searchBook";
    }

    @GetMapping("/search/result")
    public String bookList(@RequestParam(defaultValue = "") String keyword,
                             @PageableDefault(sort = "email", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model){
//        Page<Member> memberList = memberService.memberUserLoanList(keyword, pageable);
//
//        model.addAttribute("memberList", memberList);
//        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
//        model.addAttribute("next", pageable.next().getPageNumber());
//        model.addAttribute("hasNext", memberList.hasNext());
//        model.addAttribute("hasPrev", memberList.hasPrevious());

        return "book/searchBookList";
    }
}
