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
}
