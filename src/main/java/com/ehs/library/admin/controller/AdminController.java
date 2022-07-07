package com.ehs.library.admin.controller;

import com.ehs.library.book.constant.BookHopeState;
import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.book.entity.BookHope;
import com.ehs.library.book.repository.BookRepository;
import com.ehs.library.book.service.BookHopeService;
import com.ehs.library.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;
    private final BookHopeService bookHopeService;

    @GetMapping("/book/new")
    public String bookForm(Model model){
        model.addAttribute("bookFormDto", new BookFormDto());
        return "admin/addBookForm";
    }

    @PostMapping(value = "/book/new")
    public String addNewBook(@Valid BookFormDto bookFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("bookImgFile") MultipartFile bookImgFile,
                             Principal principal){

        if(bindingResult.hasErrors()){
            return "admin/addBookForm";
        }

        if(bookImgFile.isEmpty()){
            model.addAttribute("errorMessage", "이미지는 필수 입력 값 입니다.");
            return "admin/addBookForm";
        }

        try {
            String email = principal.getName();
            bookService.saveItem(bookFormDto, bookImgFile, email);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "admin/addBookForm";
        }

        return "redirect:/";
    }

    @GetMapping("mypage")
    public String adminPage(){
        return "admin/mypage";
    }

    @GetMapping("/book/hope")
    public String manageBookHope(Model model){
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

        return "admin/manageBookHope";
    }

    @GetMapping("/book/hope/review")
    public String manageBookHopeReview(Model model){
        List<BookHope> bookHopeReviewList = bookHopeService.findByState(BookHopeState.REVIEW);
        model.addAttribute("bookHopeReviewList", bookHopeReviewList);
        return "admin/manageBookHopeReview";
    }

    @GetMapping("/book/hope/reject")
    public String manageBookHopeReject(Model model){
        List<BookHope> bookHopeRejectList = bookHopeService.findByState(BookHopeState.REJECT);
        model.addAttribute("bookHopeRejectList", bookHopeRejectList);
        return "admin/manageBookHopeReject";
    }

    @GetMapping("/book/hope/allow")
    public String manageBookHopeAllow(Model model){
        List<BookHope> bookHopeAllowList = bookHopeService.findByState(BookHopeState.ALLOW);
        model.addAttribute("bookHopeAllowList", bookHopeAllowList);
        return "admin/manageBookHopeAllow";
    }

    @GetMapping("/book/hope/shipping")
    public String manageBookHopeShpping(Model model){
        List<BookHope> bookHopeShippingList = bookHopeService.findByState(BookHopeState.SHIPPING);
        model.addAttribute("bookHopeShippingList", bookHopeShippingList);
        return "admin/manageBookHopeShipping";
    }

    @GetMapping("/book/hope/arrange")
    public String manageBookHopeArrange(Model model){
        List<BookHope> bookHopeArrangeList = bookHopeService.findByState(BookHopeState.ARRANGE);
        model.addAttribute("bookHopeArrangeList",bookHopeArrangeList);
        return "admin/manageBookHopeArrange";
    }

    @GetMapping("/book/hope/complete")
    public String manageBookHopeComplete(Model model){
        List<BookHope> bookHopeCompleteList = bookHopeService.findByState(BookHopeState.COMPLETE);
        model.addAttribute("bookHopeCompleteList",bookHopeCompleteList);
        return "admin/manageBookHopeComplete";
    }
}
