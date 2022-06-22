package com.ehs.library.controller;

import com.ehs.library.dto.MemberFormDto;
import com.ehs.library.entity.Member;
import com.ehs.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/mypage")
    public String mypageIndex(){
        return "member/mypage";
    }

    @GetMapping("/signup")
    public String memberAddForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMemberForm";
    }

    @PostMapping("/signup")
    public String memberAdd(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/addMemberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMemberForm";
        }

        return "redirect:/";
    }
}
