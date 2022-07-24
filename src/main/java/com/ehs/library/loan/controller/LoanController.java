package com.ehs.library.loan.controller;


import com.ehs.library.loan.vo.Criteria;
import com.ehs.library.loan.vo.PageMaker;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanController {

    private final MemberService memberService;

    @GetMapping("/admin/book/loan")
    public String memberList(Model model){
        List<Member> memberList = memberService.findByRole(Role.USER);

        model.addAttribute("memberList", memberList);

        return "loan/loanMemberList";
    }

    @GetMapping("/admin/member/{id}")
    public String memberDetail(@PathVariable Long id, Model model){
        System.out.println(id);
        return "loan/loanMemberDatail";
    }
}
