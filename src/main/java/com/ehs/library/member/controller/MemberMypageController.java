package com.ehs.library.member.controller;

import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.dto.LoanMapperDto;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.repository.LoanMapperRepository;
import com.ehs.library.loan.service.LoanService;
import com.ehs.library.loan.vo.LoanVo;
import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/member/mypage")
@RequiredArgsConstructor
public class MemberMypageController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoanService loanService;
    private final LoanMapperRepository loanMapperRepository;

    @GetMapping("")
    public String mypageIndex(Model model, Principal principal){
        Member member = memberService.findByemail(principal.getName());
        model.addAttribute("member", member);

        return "member/mypage";
    }

    @GetMapping("/edit/confirm")
    public String myInfoEditConfirm(){
        return "member/editMemberConfirm";
    }

    @PostMapping("/edit/confirm")
    public String myInfoEditConfirmPost(@RequestParam("password") String password, Model model, Principal principal){
        Member member = memberService.findByemail(principal.getName());
        if(!passwordEncoder.matches(password, member.getPassword())){

            return "member/editMemberConfirm";
        }

        MemberEditFormDto memberEditFormDto = new MemberEditFormDto(member);
        model.addAttribute("memberEditFormDto", memberEditFormDto);
        return "member/editMemberForm";
    }

    @PostMapping("/edit")
    public String myInfoEdit(@Valid MemberEditFormDto memberEditFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/editMemberForm";
        }

        try {
            System.out.println(memberEditFormDto.getPassword());
            Member member = Member.createMember(memberEditFormDto, passwordEncoder);
            memberService.updateMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/editMemberForm";
        }

        return "redirect:/member/mypage";
    }

    @GetMapping("/loan")
    public String memberLoanBookList(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<LoanVo> loanList = loanMapperRepository.findLoanBookList(member.getId());

        model.addAttribute("loanList", loanList);

        return "member/memberLoanList";
    }

    @GetMapping("/overdue")
    public String memberLoanOverdueList(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<Loan> loanList = loanService.findByMemberAndLoan(member, LoanState.LOAN);

        model.addAttribute("loanList", loanList);

        return "member/memberLoanOverdueList";
    }
}
