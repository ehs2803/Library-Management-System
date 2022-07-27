package com.ehs.library.loan.controller;


import com.ehs.library.loan.service.LoanService;
import com.ehs.library.loan.vo.Criteria;
import com.ehs.library.loan.vo.PageMaker;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.synth.SynthUI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanController {

    private final MemberService memberService;

    private final LoanService loanService;

    @GetMapping("/admin/book/loan")
    public String memberList(@RequestParam(defaultValue = "") String keyword,
                             @PageableDefault(sort = "email", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model){
        Page<Member> memberList = memberService.memberUserLoanList(keyword, pageable);

        model.addAttribute("memberList", memberList);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", memberList.hasNext());
        model.addAttribute("hasPrev", memberList.hasPrevious());

        return "loan/loanMemberList";
    }

    @GetMapping("/admin/member/{id}")
    public String memberDetail(@PathVariable Long id, Model model){
        Member member = memberService.findById(id);
        model.addAttribute("member", member);

        return "loan/loanMemberDatail";
    }
}
