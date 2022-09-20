package com.ehs.library.member.controller;

import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.exception.UserAlreadyExistException;
import com.ehs.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String memberAddForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMemberForm";
    }

    // 회원가입 요청
    @PostMapping("/signup")
    public String memberAdd(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){ // 검증 문제 발생 시
            log.debug("회원가입 검증 문제 발생");
            return "member/addMemberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (UserAlreadyExistException e){
            log.debug("회원가입 실패 : 이미존재하는 이메일");
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMemberForm";
        }

        return "redirect:/member/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginMember(){
        return "member/loginForm";
    }

    // 로그인 시도 -> 에러발생 시
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        log.debug("로그인 실패");
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/loginForm";
    }
}
