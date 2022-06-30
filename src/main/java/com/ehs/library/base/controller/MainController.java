package com.ehs.library.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String main(){
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
