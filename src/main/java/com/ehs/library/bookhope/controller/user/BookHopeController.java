package com.ehs.library.bookhope.controller.user;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.dto.BookHopeMapperDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeMapperRepository;
import com.ehs.library.bookhope.service.user.BookHopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller("user.BookHopeController")
@RequiredArgsConstructor
public class BookHopeController {
    private final BookHopeService bookHopeService;

    private final BookHopeMapperRepository bookHopeMapperRepository;

    @GetMapping("/book/hope/register")
    public String registerHopeBookForm(Model model){
        model.addAttribute("bookHopeFormDto", new BookHopeFormDto());
        return "book/addHopeBookForm";
    }

    @PostMapping("/book/hope/register")
    public String registerHopeBook(@Valid BookHopeFormDto bookHopeFormDto, BindingResult bindingResult,
                                   Model model, Principal principal){
        if(bindingResult.hasErrors()){
            return "book/addHopeBookForm";
        }

        try {
            String email = principal.getName();
            bookHopeService.registerBookHope(bookHopeFormDto, email);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "book/addHopeBookForm";
        }

        return "redirect:/member/mypage";
    }

    @GetMapping("/member/mypage/book/hope/list")
    public String memberBookHopeList(Model model, Principal principal){
        String email = principal.getName();

        List<BookHope> bookHopeListComplete = bookHopeService.findByMemberAndState(email, BookHopeState.COMPLETE);
        List<BookHope> bookHopeListReject = bookHopeService.findByMemberAndState(email, BookHopeState.REJECT);
        List<BookHopeMapperDto> bookHopeMapperDtoList = bookHopeMapperRepository.findProgressBookHope(3L);

//        for(int i=0;i<bookHopeMapperDtoList.size();i++){
//            System.out.println(bookHopeMapperDtoList.get(i).getBook_hope_id());
//        }
//        for(int i=0;i<bookHopeListComplete.size();i++){
//            System.out.println(bookHopeListComplete.get(i).getName());
//        }
//        for(int i=0;i<bookHopeListReject.size();i++){
//            System.out.println(bookHopeListReject.get(i).getName());
//        }

        model.addAttribute("bookHopeListComplete", bookHopeListComplete);
        model.addAttribute("bookHopeListReject", bookHopeListReject);
        model.addAttribute("bookHopeListProgress", bookHopeMapperDtoList);

        return "bookhope/user/userBookHopeList";
    }
}
