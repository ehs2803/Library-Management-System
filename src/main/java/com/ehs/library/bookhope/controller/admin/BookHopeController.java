package com.ehs.library.bookhope.controller.admin;

import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.book.service.BookService;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeDetailDto;
import com.ehs.library.bookhope.dto.BookHopeDto;
import com.ehs.library.bookhope.dto.BookHopeUpdateDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.service.admin.BookHopeService;
import com.ehs.library.loan.dto.LoanDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("admin.BookHopeController")
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class BookHopeController {

    private final BookHopeService bookHopeService;
    private final ModelMapper modelMapper;

    // 도서관 직원 관리자페이지
    @GetMapping("mypage")
    public String adminPage(){
        return "admin/mypage";
    }

    // 도서관 직원 관리자페이지 희망도서 신청 데시보드
    @GetMapping("/book/hope")
    public String manageBookHope(Model model){
        // 각 상태별 수
        int reviewCnt = bookHopeService.countByState(BookHopeState.REVIEW);
        int rejectCnt = bookHopeService.countByState(BookHopeState.REJECT);
        int allowCnt = bookHopeService.countByState(BookHopeState.ALLOW);
        int shippingCnt = bookHopeService.countByState(BookHopeState.SHIPPING);
        int arrangeCnt = bookHopeService.countByState(BookHopeState.ARRANGE);
        int completeCnt = bookHopeService.countByState(BookHopeState.COMPLETE);

        model.addAttribute("reviewCnt", reviewCnt);
        model.addAttribute("rejectCnt", rejectCnt);
        model.addAttribute("allowCnt", allowCnt);
        model.addAttribute("shippingCnt", shippingCnt);
        model.addAttribute("arrangeCnt", arrangeCnt);
        model.addAttribute("completeCnt", completeCnt);

        return "bookhope/admin/manageBookHope";
    }

    // 희망도서 신청 상태가 REVIEW인 목록 조회
    @GetMapping("/book/hope/review")
    public String manageBookHopeReview(Model model){
        List<BookHope> bookHopeReviewList_entity = bookHopeService.findByState(BookHopeState.REVIEW);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeReviewList = bookHopeReviewList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeReviewList", bookHopeReviewList);
        return "bookhope/admin/manageBookHopeReview";
    }

    // bookhope update 폼
    @GetMapping("/book/hope/update/{id}")
    public String BookHopeReviewUpdate(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id).get();

        BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                .id(bookHope_entity.getId())
                .name(bookHope_entity.getName())
                .isbn(bookHope_entity.getIsbn())
                .author(bookHope_entity.getAuthor())
                .publisher(bookHope_entity.getPublisher())
                .year(bookHope_entity.getYear())
                .state(bookHope_entity.getState().toString())
                .build();

        model.addAttribute("bookHope", bookHope);

        return "bookhope/admin/manageBookHopeUpdate";
    }

    // bookhope state update
    @PostMapping("/book/hope/update")
    public String BookHopeReviewUpdatePost(BookHopeUpdateDto bookHopeUpdateDto, Model model){

        // 상태를 REJECT으로 update하는데, failReason이 빈 문자열일 경우
        if(bookHopeUpdateDto.getState().toString().equals("REJECT")&&bookHopeUpdateDto.getFailReason().equals("")){
            BookHope bookHope_entity = bookHopeService.findById(bookHopeUpdateDto.getId()).get();

            BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                    .id(bookHope_entity.getId())
                    .name(bookHope_entity.getName())
                    .isbn(bookHope_entity.getIsbn())
                    .author(bookHope_entity.getAuthor())
                    .publisher(bookHope_entity.getPublisher())
                    .year(bookHope_entity.getYear())
                    .state(bookHope_entity.getState().toString())
                    .build();

            model.addAttribute("bookHope", bookHope);
            model.addAttribute("errorMessage", "거절 시 거절사유 입력은 필수입니다.");
            return "bookhope/admin/manageBookHopeUpdate";
        }

        try {
            bookHopeService.updateState(bookHopeUpdateDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상태 변경 중 에러가 발생하였습니다.");
            BookHope bookHope_entity = bookHopeService.findById(bookHopeUpdateDto.getId()).get();
            BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                    .id(bookHope_entity.getId())
                    .name(bookHope_entity.getName())
                    .isbn(bookHope_entity.getIsbn())
                    .author(bookHope_entity.getAuthor())
                    .publisher(bookHope_entity.getPublisher())
                    .year(bookHope_entity.getYear())
                    .state(bookHope_entity.getState().toString())
                    .build();

            model.addAttribute("bookHope", bookHope);
            return "bookhope/admin/manageBookHopeUpdate";
        }

        return "redirect:/admin/book/hope";
    }

    // bookhope 상태가 REJECT인 목록 조회
    @GetMapping("/book/hope/reject")
    public String manageBookHopeReject(Model model){
        List<BookHope> bookHopeRejectList_entity = bookHopeService.findByState(BookHopeState.REJECT);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeRejectList = bookHopeRejectList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeRejectList", bookHopeRejectList);
        return "bookhope/admin/manageBookHopeReject";
    }

    // bookhope 상태가 ALLOW인 목록 조회
    @GetMapping("/book/hope/allow")
    public String manageBookHopeAllow(Model model){
        List<BookHope> bookHopeAllowList_entity = bookHopeService.findByState(BookHopeState.ALLOW);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeAllowList = bookHopeAllowList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeAllowList", bookHopeAllowList);
        return "bookhope/admin/manageBookHopeAllow";
    }

    // bookhope 상태가 SHIPPING인 목록 조회
    @GetMapping("/book/hope/shipping")
    public String manageBookHopeShpping(Model model){
        List<BookHope> bookHopeShippingList_entity = bookHopeService.findByState(BookHopeState.SHIPPING);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeShippingList = bookHopeShippingList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeShippingList", bookHopeShippingList);
        return "bookhope/admin/manageBookHopeShipping";
    }

    // bookhope 상태가 ARRANGE인 목록 조회
    @GetMapping("/book/hope/arrange")
    public String manageBookHopeArrange(Model model){
        List<BookHope> bookHopeArrangeList_entity = bookHopeService.findByState(BookHopeState.ARRANGE);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeArrangeList = bookHopeArrangeList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeArrangeList",bookHopeArrangeList);
        return "bookhope/admin/manageBookHopeArrange";
    }

    // bookhope 상태가 COMPLETE인 목록 조회
    @GetMapping("/book/hope/complete")
    public String manageBookHopeComplete(Model model){
        List<BookHope> bookHopeCompleteList_entity = bookHopeService.findByState(BookHopeState.COMPLETE);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeCompleteList = bookHopeCompleteList_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeCompleteList",bookHopeCompleteList);
        return "bookhope/admin/manageBookHopeComplete";
    }

    // bookhope state가 complete인 목록 상세 조회
    @GetMapping("/book/hope/complete/{id}")
    public String BookHopeCompleteDetail(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id).get();
        BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                .id(bookHope_entity.getId())
                .name(bookHope_entity.getName())
                .isbn(bookHope_entity.getIsbn())
                .author(bookHope_entity.getAuthor())
                .publisher(bookHope_entity.getPublisher())
                .year(bookHope_entity.getYear())
                .state(bookHope_entity.getState().toString())
                .allowTime(bookHope_entity.getAllowTime())
                .completeTime(bookHope_entity.getCompleteTime())
                .shippingTime(bookHope_entity.getShippingTime())
                .arrangeTime(bookHope_entity.getArrangeTime())
                .build();

        model.addAttribute("bookHope", bookHope);

        return "bookhope/admin/manageBookHopeCompleteDetail";
    }

    // bookhope state가 reject인 목록 상세 조회
    @GetMapping("/book/hope/reject/{id}")
    public String BookHopeRejectDetail(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id).get();
        BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                .id(bookHope_entity.getId())
                .name(bookHope_entity.getName())
                .isbn(bookHope_entity.getIsbn())
                .author(bookHope_entity.getAuthor())
                .publisher(bookHope_entity.getPublisher())
                .year(bookHope_entity.getYear())
                .state(bookHope_entity.getState().toString())
                .failReason(bookHope_entity.getFailReason())
                .rejectTime(bookHope_entity.getRejectTime())
                .build();

        model.addAttribute("bookHope", bookHope);

        return "bookhope/admin/manageBookHopeRejectDetail";
    }

    // id bookhope 신청에 대해서 새로운 책 등록하기
    @GetMapping("/book/hope/new/{id}")
    public String bookHopeRegisterForm(@PathVariable Long id, Model model){
        BookHope bookHope = bookHopeService.findById(id).get();

        BookFormDto bookFormDto = BookFormDto.builder()
                .name(bookHope.getName())
                .isbn(bookHope.getIsbn())
                .author(bookHope.getAuthor())
                .publisher(bookHope.getPublisher())
                .year(bookHope.getYear())
                .build();

        model.addAttribute("bookFormDto", bookFormDto);

        return "admin/addBookForm";
    }
}
