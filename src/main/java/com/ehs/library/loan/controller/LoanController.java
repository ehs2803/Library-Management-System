package com.ehs.library.loan.controller;


import com.ehs.library.book.dto.BookListDto;
import com.ehs.library.book.service.BookService;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.dto.LoanDto;
import com.ehs.library.loan.dto.LoanWaitDto;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.entity.LoanWaitList;
import com.ehs.library.loan.service.LoanService;
import com.ehs.library.loan.vo.Criteria;
import com.ehs.library.loan.vo.PageMaker;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.dto.MemberDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthUI;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LoanController {

    private final MemberService memberService;
    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    // 일반 유저 검색(키워드, 페이징)
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

    // 멤버 상세 페이지 (대출내역)
    @GetMapping("/admin/member/{id}")
    public String memberDetail(@PathVariable Long id, Model model){
        Member member_entity = memberService.findById(id);
        MemberDto member = MemberDto.builder()
                .id(member_entity.getId())
                .email(member_entity.getEmail())
                .name(member_entity.getName())
                .tel(member_entity.getTel())
                .address(member_entity.getAddress())
                .regTime(member_entity.getRegTime())
                .build();

        List<LoanWaitList> loanWaitList_entity = loanService.findByMember(member_entity);
        List<Loan> loanList_entity = loanService.findByMemberAndLoan(member_entity, LoanState.LOAN);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<LoanDto> loanList = loanList_entity.stream()
                .map(loan->modelMapper.map(loan, LoanDto.class))
                .collect(Collectors.toList());
        List<LoanWaitDto> loanWaitList = loanWaitList_entity.stream()
                .map(loan->modelMapper.map(loan, LoanWaitDto.class))
                .collect(Collectors.toList());

        model.addAttribute("member", member);
        model.addAttribute("loanWaitList", loanWaitList);
        model.addAttribute("loanList", loanList);

        return "loan/loanMemberDatail";
    }

    // 책 이름으로 도서 검색해 json으로 결과 받기
    @ResponseBody
    @PostMapping(value = "/api/book/search/base")
    public String getSaleWeek(@RequestParam Map<String, Object> params) throws IOException {
        String keyword = (String) params.get("keyword");

        return bookService.findByNameContainingRetrunJson(keyword);
    }

    // 검색한 도서 waitList에 추가하기
    @GetMapping(value = "/admin/book/loan/{mid}/{bid}")
    public String moveReadyLoanList(@PathVariable Long mid,@PathVariable Long bid){
        Member member = memberService.findById(mid);
        loanService.moveWaitList(member, bid);

        return "redirect:/admin/member/"+mid;
    }

    // waitList에 있는 특정 도서 삭제하기
    @GetMapping(value = "/admin/book/loan/delete/{mid}/{wid}")
    public String deleteReadyLoanList(@PathVariable Long mid,@PathVariable Long wid){
        loanService.deleteWaitList(wid);

        return "redirect:/admin/member/"+mid;
    }

    // waitList에 있는 도서 대출하기
    @GetMapping(value = "/admin/book/loan/{mid}")
    public String loanWaitBookList(@PathVariable Long mid, Model model) {
        Member member = memberService.findById(mid);

        try {
            loanService.loanWatiBookList(member);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "loan/loanMemberDatail";
        }

        return "redirect:/admin/member/"+mid;
    }

    // 도서 반납
    @GetMapping(value = "/admin/book/loan/return/{id}")
    public String loanReturn(@PathVariable Long id){
        Long mid = loanService.loanReturn(id);

        return "redirect:/admin/member/"+mid;
    }
}
