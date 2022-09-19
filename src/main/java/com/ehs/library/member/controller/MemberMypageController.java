package com.ehs.library.member.controller;

import com.ehs.library.bookinterest.dto.BookInterestDto;
import com.ehs.library.bookinterest.entity.BookInterest;
import com.ehs.library.bookinterest.repository.BookInterestRepository;
import com.ehs.library.bookreservation.dto.BookReservationDto;
import com.ehs.library.bookreservation.entity.BookReservation;
import com.ehs.library.bookreservation.repository.BookReservationRepository;
import com.ehs.library.loan.constant.LoanState;
import com.ehs.library.loan.dto.LoanMapperDto;
import com.ehs.library.loan.entity.Loan;
import com.ehs.library.loan.repository.LoanMapperRepository;
import com.ehs.library.loan.service.LoanService;
import com.ehs.library.loan.vo.LoanVo;
import com.ehs.library.member.dto.MemberDto;
import com.ehs.library.member.dto.MemberEditFormDto;
import com.ehs.library.member.dto.MemberFormDto;
import com.ehs.library.member.entity.Member;
import com.ehs.library.member.service.MemberService;
import com.ehs.library.roomreservation.dto.StudyRoomReservationDto;
import com.ehs.library.roomreservation.entity.StudyRoomReservation;
import com.ehs.library.roomreservation.service.user.RoomReservationService;
import com.ehs.library.sanction.dto.SanctionDto;
import com.ehs.library.sanction.entity.Sanction;
import com.ehs.library.sanction.service.SanctionService;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/member/mypage")
@RequiredArgsConstructor
public class MemberMypageController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoanService loanService;
    private final RoomReservationService roomReservationService;
    private final SanctionService sanctionService;
    private final ModelMapper modelMapper;

    // 일반 유저 마이페이지
    @GetMapping("")
    public String mypageIndex(Model model, Principal principal){
        Member findMember = memberService.findByemail(principal.getName());
        MemberDto member = MemberDto.builder()
                .id(findMember.getId())
                .name(findMember.getName())
                .email(findMember.getEmail())
                .tel(findMember.getTel())
                .address(findMember.getAddress())
                .role(findMember.getRole().toString())
                .build();

        model.addAttribute("member", member);

        return "member/mypage";
    }

    // 마이페이지 - 회원정보수정 - 비밀번호 확인
    @GetMapping("/edit/confirm")
    public String myInfoEditConfirm(){
        return "member/editMemberConfirm";
    }

    // 마이페이지 - 회원정보수정 - 비밀번호 확인
    @PostMapping("/edit/confirm")
    public String myInfoEditConfirmPost(@RequestParam("password") String password, Model model, Principal principal){
        Member member = memberService.findByemail(principal.getName());
        // 입력 password와 DB에 저장된 password가 다를 경우
        if(!passwordEncoder.matches(password, member.getPassword())){
            return "member/editMemberConfirm";
        }

        MemberEditFormDto memberEditFormDto = new MemberEditFormDto(member);
        model.addAttribute("memberEditFormDto", memberEditFormDto);
        return "member/editMemberForm";
    }

    // 마이페이지 - 회원정보 수정
    @PostMapping("/edit")
    public String myInfoEdit(@Valid MemberEditFormDto memberEditFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/editMemberForm";
        }

        try {
            Member member = Member.createMember(memberEditFormDto, passwordEncoder);
            memberService.updateMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/editMemberForm";
        }

        return "redirect:/member/mypage";
    }

    // 대출 내역 조회
    @GetMapping("/loan")
    public String memberLoanBookList(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<LoanVo> loanList = memberService.findLoanBookList(member);

        model.addAttribute("loanList", loanList);

        return "member/memberLoanList";
    }

    // 연체 내역 조회
    @GetMapping("/overdue")
    public String memberLoanOverdueList(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<LoanVo> loanList = memberService.findLoanOverdueList(member);

        model.addAttribute("loanList", loanList);

        return "member/memberLoanOverdueList";
    }

    // 대출기간 연장
    @GetMapping("/delay/{id}")
    public String memberBookLoanDelay(@PathVariable Long id, Model model){
        try {
            loanService.delayLoan(id);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberLoanList";
        }


        return "redirect:/member/mypage/loan";
    }

    // 나의서제(관심도서) 내역 조회
    @GetMapping("/interest")
    public String memberBookInterest(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<BookInterest> bookInterestList_temp = memberService.findByMemberBookInterestList(member);

        //ModelMapper modelMapper = new ModelMapper(); // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookInterestDto> bookInterestList = bookInterestList_temp.stream()
                .map(book-> modelMapper.map(book, BookInterestDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookInterestList", bookInterestList);

        return "member/memberBookInterestList";
    }

    // 도서 예약 조회
    @GetMapping("/reservation")
    public String memberBookReservation(Principal principal, Model model){
        Member member = memberService.findByemail(principal.getName());
        List<BookReservation> bookReservationList_temp = memberService.findByMemberBookReservation(member);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookReservationDto> bookReservationList = bookReservationList_temp.stream()
                .map(reservation-> modelMapper.map(reservation, BookReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookReservationList", bookReservationList);

        return "member/memberBookReservationList";
    }

    // 스터디룸 예약 신청 취소
    @GetMapping("/reservation/studyroom/cancel/{id}")
    public String memberReservationStudyRoomCancel(@PathVariable Long id, Principal principal, Model model){
        try {
            roomReservationService.studyRoomStateSetCancel(id);
        } catch (Exception e){
            List<StudyRoomReservation> ReservationList = roomReservationService.findByMemberFetchJoinRoom(principal.getName());

            List<StudyRoomReservation> reservationList_wait_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_reject_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_allow_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_use_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_cancel_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_complete_temp = new ArrayList<>();
            List<StudyRoomReservation> reservationList_noshow_temp = new ArrayList<>();
            // 예약 상태로 분류
            for(int i=0;i<ReservationList.size();i++){
                if(ReservationList.get(i).getState().toString().equals("WAIT")) reservationList_wait_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("REJECT")) reservationList_reject_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("ALLOW")) reservationList_allow_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("USE")) reservationList_use_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("CANCEL")) reservationList_cancel_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("COMPLETE")) reservationList_complete_temp.add(ReservationList.get(i));
                else if(ReservationList.get(i).getState().toString().equals("NOSHOW")) reservationList_noshow_temp.add(ReservationList.get(i));
            }

            // ModelMapper이용해 List<Entity> -> List<Dto>
            List<StudyRoomReservationDto> reservationList_wait = reservationList_wait_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_reject = reservationList_reject_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_allow = reservationList_allow_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_use = reservationList_use_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_cancel = reservationList_cancel_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_complete = reservationList_complete_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());
            List<StudyRoomReservationDto> reservationList_noshow = reservationList_noshow_temp.stream()
                    .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                    .collect(Collectors.toList());

            model.addAttribute("reservationList_wait",reservationList_wait);
            model.addAttribute("reservationList_reject",reservationList_reject);
            model.addAttribute("reservationList_allow",reservationList_allow);
            model.addAttribute("reservationList_use",reservationList_use);
            model.addAttribute("reservationList_cancel",reservationList_cancel);
            model.addAttribute("reservationList_complete",reservationList_complete);
            model.addAttribute("reservationList_noshow",reservationList_noshow);
            model.addAttribute("errorMessage", e.getMessage());

            return "member/memberStudyRoomReservationList";
        }

        return "redirect:/member/mypage/reservation/studyroom";
    }

    // 스터디룸 예약 조회
    @GetMapping("/reservation/studyroom")
    public String memberReservationStudyRoomList(Principal principal, Model model){
        List<StudyRoomReservation> ReservationList = roomReservationService.findByMemberFetchJoinRoom(principal.getName());

        List<StudyRoomReservation> reservationList_wait_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_reject_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_allow_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_use_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_cancel_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_complete_temp = new ArrayList<>();
        List<StudyRoomReservation> reservationList_noshow_temp = new ArrayList<>();
        // 예약 상태로 분류
        for(int i=0;i<ReservationList.size();i++){
            if(ReservationList.get(i).getState().toString().equals("WAIT")) reservationList_wait_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("REJECT")) reservationList_reject_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("ALLOW")) reservationList_allow_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("USE")) reservationList_use_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("CANCEL")) reservationList_cancel_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("COMPLETE")) reservationList_complete_temp.add(ReservationList.get(i));
            else if(ReservationList.get(i).getState().toString().equals("NOSHOW")) reservationList_noshow_temp.add(ReservationList.get(i));
        }

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<StudyRoomReservationDto> reservationList_wait = reservationList_wait_temp.stream()
                        .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                        .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_reject = reservationList_reject_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_allow = reservationList_allow_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_use = reservationList_use_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_cancel = reservationList_cancel_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_complete = reservationList_complete_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());
        List<StudyRoomReservationDto> reservationList_noshow = reservationList_noshow_temp.stream()
                .map(reservation-> modelMapper.map(reservation, StudyRoomReservationDto.class))
                .collect(Collectors.toList());

        model.addAttribute("reservationList_wait",reservationList_wait);
        model.addAttribute("reservationList_reject",reservationList_reject);
        model.addAttribute("reservationList_allow",reservationList_allow);
        model.addAttribute("reservationList_use",reservationList_use);
        model.addAttribute("reservationList_cancel",reservationList_cancel);
        model.addAttribute("reservationList_complete",reservationList_complete);
        model.addAttribute("reservationList_noshow",reservationList_noshow);

        return "member/memberStudyRoomReservationList";
    }


    @GetMapping("/sanction/list")
    public String getSanctionList(Model model, Principal principal){
        List<Sanction> sanctionList_book_entity = sanctionService.findBookSanctionsByMemberFetchJoin(principal.getName());
        List<Sanction> sanctionList_studyroom_entity = sanctionService.findStudyRoomSanctionsByMemberFetchJoin(principal.getName());

        // List<Entity> -> List<Dto>
        List<SanctionDto> sanctionList_book = sanctionList_book_entity.stream()
                .map(sanction -> modelMapper.map(sanction, SanctionDto.class))
                .collect(Collectors.toList());
        List<SanctionDto> sanctionList_studyroom = sanctionList_studyroom_entity.stream()
                .map(sanction -> modelMapper.map(sanction, SanctionDto.class))
                .collect(Collectors.toList());

        model.addAttribute("sanctionList_book",sanctionList_book);
        model.addAttribute("sanctionList_studyroom",sanctionList_studyroom);

        return "member/memberSanctionList";
    }
}
