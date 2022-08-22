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

    @GetMapping("")
    public String main(Model model){
        mainService.increaseHit();

        int hit = mainService.getHitPerDay();
        Long userCnt = mainService.getuserMemberCnt();
        Long bookCnt = mainService.getBookCnt();

        model.addAttribute("hit", hit);
        model.addAttribute("userCnt", userCnt);
        model.addAttribute("bookCnt", bookCnt);

        return "main/index";
    }

    @GetMapping(value = "/main/location")
    public String location(){
        return "main/location";
    }

    @GetMapping(value = "/main/time")
    public String time(){
        return "main/introTime";
    }

    @GetMapping(value = "/main/member")
    public String member(){
        return "main/introMember";
    }

    @GetMapping(value = "/main/book")
    public String book(){
        return "main/introBook";
    }

    @GetMapping(value = "/main/delivery")
    public String delivery(){
        return "main/introDelivery";
    }
}
