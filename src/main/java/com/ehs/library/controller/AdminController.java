package com.ehs.library.controller;

import com.ehs.library.dto.BookFormDto;
import com.ehs.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;

    @GetMapping("/book/new")
    public String bookForm(Model model){
        model.addAttribute("bookFormDto", new BookFormDto());
        return "admin/addBookForm";
    }

    @PostMapping(value = "/book/new")
    public String addNewBook(@Valid BookFormDto bookFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("bookImgFile") MultipartFile bookImgFile){

        if(bindingResult.hasErrors()){
            return "admin/addBookForm";
        }

        if(bookImgFile.isEmpty()){
            model.addAttribute("errorMessage", "이미지는 필수 입력 값 입니다.");
            return "admin/addBookForm";
        }

        try {
            //itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "admin/addBookForm";
        }

        return "redirect:/";
    }
}
