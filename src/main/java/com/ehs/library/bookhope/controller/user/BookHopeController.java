package com.ehs.library.bookhope.controller.user;

import com.ehs.library.book.entity.Book;
import com.ehs.library.bookapi.dto.BookApiResultDto;
import com.ehs.library.bookapi.paging.Criteria;
import com.ehs.library.bookapi.paging.PageMaker;
import com.ehs.library.bookapi.service.BookApiService;
import com.ehs.library.bookhope.constant.BookHopeState;
import com.ehs.library.bookhope.dto.BookHopeDetailDto;
import com.ehs.library.bookhope.dto.BookHopeDto;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.dto.BookHopeMapperDto;
import com.ehs.library.bookhope.entity.BookHope;
import com.ehs.library.bookhope.repository.BookHopeMapperRepository;
import com.ehs.library.bookhope.service.user.BookHopeService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("user.BookHopeController")
@RequiredArgsConstructor
public class BookHopeController {
    private final BookHopeService bookHopeService;
    private final BookHopeMapperRepository bookHopeMapperRepository;
    private final ModelMapper modelMapper;

    private final BookApiService bookApiService;

    // 네이버 book api 검색 폼
    @GetMapping("/book/hope/search")
    public String searchBookAPIForm(){
        return "book/api/searchBookHope";
    }

    // 네이버 book api 검색 결과 v2 - 페이징
    @GetMapping("/book/hope/search/list")
    public String seachBookAPI(@RequestParam(defaultValue = "") String keyword,
                               Criteria cri,
                               Model model){
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCri(cri);
        BookApiResultDto books = bookApiService.searchBookNaverAPI(keyword, cri);
        pageMaker.setTotalCount(books.getTotal());

        model.addAttribute("pageMaker",pageMaker);
        model.addAttribute("keyword", keyword);
        model.addAttribute("books", books);

        return "book/api/searchBookHopeResult";
    }

    // 네이버 book api 검색 결과 v1
//    @GetMapping("/book/hope/search/list")
//    public String seachBookAPI(@RequestParam(defaultValue = "") String keyword, Model model){
//        BookApiResultDto books = bookApiService.searchBookNaverAPI(keyword);
//
//        model.addAttribute("books", books);
//
//        return "book/api/searchBookHopeResult";
//    }

    @GetMapping("/book/hope/register/api")
    public String registerHopeBookApiForm(@ModelAttribute BookHopeFormDto bookHopeFormDto, Model model){
        String pubdate = String.valueOf(bookHopeFormDto.getYear());
        int year = Integer.parseInt(pubdate.substring(0,4));
        bookHopeFormDto.setYear(year);

        model.addAttribute("bookHopeFormDto", bookHopeFormDto);
        return "book/addHopeBookForm";
    }

    // 희망도서 신청 폼
    @GetMapping("/book/hope/register")
    public String registerHopeBookForm(Model model){
        model.addAttribute("bookHopeFormDto", new BookHopeFormDto());
        return "book/addHopeBookForm";
    }

    // 희망도서 신청
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

    // 멤버 마이페이지에서 희망도서 신청 목록 조회
    @GetMapping("/member/mypage/book/hope/list")
    public String memberBookHopeList(Model model, Principal principal){
        String email = principal.getName();
        Long memberId = bookHopeService.getMemberId(email);

        List<BookHope> bookHopeListComplete_entity = bookHopeService.findByMemberAndState(email, BookHopeState.COMPLETE);
        List<BookHope> bookHopeListReject_entity = bookHopeService.findByMemberAndState(email, BookHopeState.REJECT);
        List<BookHopeMapperDto> bookHopeMapperDtoList = bookHopeMapperRepository.findProgressBookHope(memberId);

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookHopeDto> bookHopeListComplete = bookHopeListComplete_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());
        List<BookHopeDto> bookHopeListReject = bookHopeListReject_entity.stream()
                .map(bookHope->modelMapper.map(bookHope, BookHopeDto.class))
                .collect(Collectors.toList());

        model.addAttribute("bookHopeListComplete", bookHopeListComplete);
        model.addAttribute("bookHopeListReject", bookHopeListReject);
        model.addAttribute("bookHopeListProgress", bookHopeMapperDtoList);

        return "bookhope/user/userBookHopeList";
    }

    // 거절된 희망도서 신청 상세보기
    @GetMapping("/member/mypage/book/hope/reject/{id}")
    public String memberBookHopeReject(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id);
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

        return "bookhope/user/BookHopeRejectDetail";
    }

    // 완료처리된 희망도서 신청 상세보기
    @GetMapping("/member/mypage/book/hope/complete/{id}")
    public String memberBookHopeComplete(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id);
        BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                .id(bookHope_entity.getId())
                .name(bookHope_entity.getName())
                .isbn(bookHope_entity.getIsbn())
                .author(bookHope_entity.getAuthor())
                .publisher(bookHope_entity.getPublisher())
                .year(bookHope_entity.getYear())
                .state(bookHope_entity.getState().toString())
                .allowTime(bookHope_entity.getAllowTime())
                .shippingTime(bookHope_entity.getShippingTime())
                .arrangeTime(bookHope_entity.getArrangeTime())
                .completeTime(bookHope_entity.getCompleteTime())
                .build();

        model.addAttribute("bookHope", bookHope);

        return "bookhope/user/BookHopeCompleteDetail";
    }

    // 진행중인 희망도서 신청 상세보기
    @GetMapping("/member/mypage/book/hope/progress/{id}")
    public String memberBookHopeProgress(@PathVariable Long id, Model model){
        BookHope bookHope_entity = bookHopeService.findById(id);
        BookHopeDetailDto bookHope = BookHopeDetailDto.builder()
                .id(bookHope_entity.getId())
                .name(bookHope_entity.getName())
                .isbn(bookHope_entity.getIsbn())
                .author(bookHope_entity.getAuthor())
                .publisher(bookHope_entity.getPublisher())
                .year(bookHope_entity.getYear())
                .state(bookHope_entity.getState().toString())
                .allowTime(bookHope_entity.getAllowTime())
                .shippingTime(bookHope_entity.getShippingTime())
                .arrangeTime(bookHope_entity.getArrangeTime())
                .build();

        model.addAttribute("bookHope", bookHope);

        return "bookhope/user/BookHopeProgressDetail";
    }
}
