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
        List<BookHope> bookHopeReviewList = bookHopeService.findByState(BookHopeState.REVIEW);
        List<BookHope> bookHopeRejectList = bookHopeService.findByState(BookHopeState.REJECT);
        List<BookHope> bookHopeAllowList = bookHopeService.findByState(BookHopeState.ALLOW);
        List<BookHope> bookHopeShippingList = bookHopeService.findByState(BookHopeState.SHIPPING);
        List<BookHope> bookHopeArrangeList = bookHopeService.findByState(BookHopeState.ARRANGE);
        List<BookHope> bookHopeCompleteList = bookHopeService.findByState(BookHopeState.COMPLETE);

        model.addAttribute("bookHopeReviewList", bookHopeReviewList);
        model.addAttribute("bookHopeRejectList", bookHopeRejectList);
        model.addAttribute("bookHopeAllowList", bookHopeAllowList);
        model.addAttribute("bookHopeShippingList", bookHopeShippingList);
        model.addAttribute("bookHopeArrangeList",bookHopeArrangeList);
        model.addAttribute("bookHopeCompleteList",bookHopeCompleteList);

        return "admin/manageBookHope";
    }
}
