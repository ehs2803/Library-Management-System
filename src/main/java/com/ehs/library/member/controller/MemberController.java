package com.ehs.library.member.controller;

import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
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

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String loginMember(){
        return "/member/loginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/loginForm";
    }
}
