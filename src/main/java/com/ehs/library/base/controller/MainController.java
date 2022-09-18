package com.ehs.library.base.controller;

import com.ehs.library.base.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 메인페이지
    @GetMapping("")
    public String main(Model model){
        mainService.increaseHit(); // 당일 조회수 증가

        int hit = mainService.getHitPerDay(); // 당일 조회수
        Long userCnt = mainService.getuserMemberCnt(); // 일반 유저 수
        Long bookCnt = mainService.getBookCnt(); // db에 저장된 도서 수

        model.addAttribute("hit", hit);
        model.addAttribute("userCnt", userCnt);
        model.addAttribute("bookCnt", bookCnt);

        return "main/index";
    }

    // 도서관 위치 정보 페이지
    @GetMapping(value = "/main/location")
    public String location(){
        return "main/location";
    }

    // 도서관 운영시간 정보 페이지
    @GetMapping(value = "/main/time")
    public String time(){
        return "main/introTime";
    }

    // 도서관 회원 정책 정보 페이지
    @GetMapping(value = "/main/member")
    public String member(){
        return "main/introMember";
    }

    // 도서관 도서 정책 정보 페이지
    @GetMapping(value = "/main/book")
    public String book(){
        return "main/introBook";
    }

}
