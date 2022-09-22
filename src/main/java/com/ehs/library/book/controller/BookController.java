package com.ehs.library.book.controller;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.book.dto.BookFormDto;
import com.ehs.library.book.dto.BookListDto;
import com.ehs.library.book.dto.BookSearchCondition;
import com.ehs.library.book.entity.Book;
import com.ehs.library.book.service.BookService;
import com.ehs.library.bookapi.dto.BookApiResultDto;
import com.ehs.library.bookapi.paging.Criteria;
import com.ehs.library.bookapi.paging.PageMaker;
import com.ehs.library.bookapi.service.BookApiService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    private final BookApiService bookApiService;

    // 새로운 책 등록 폼
    @GetMapping("/admin/book/new")
    public String bookForm(Model model){
        model.addAttribute("bookFormDto", new BookFormDto());
        return "admin/addBookForm";
    }

    // 새로운 책 등록
    @PostMapping(value = "/admin/book/new")
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

    // 도서 통합검색 페이지
    @GetMapping(value = "/main/book/search")
    public String searchBookList(){
        return "book/searchBook";
    }

    // 도서 검색 v1 - 키워드검색, 페이징 처리
//    @GetMapping("/main/book/search/result")
//    public String bookList(@RequestParam(defaultValue = "") String keyword,
//                           @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
//                           Model model){
//        Page<Book> bookList = bookService.searchBookList(keyword, pageable);
//
//        model.addAttribute("bookList", bookList);
//        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
//        model.addAttribute("next", pageable.next().getPageNumber());
//        model.addAttribute("hasNext", bookList.hasNext());
//        model.addAttribute("hasPrev", bookList.hasPrevious());
//
//        return "book/searchBookList";
//    }

    // 도서 검색 v2 - 통합검색, 동적쿼리
//    @GetMapping("/main/book/search/result")
//    public String bookList(@RequestParam(defaultValue = "") String keyword,
//                           @RequestParam(defaultValue = "") String isbn,
//                           @RequestParam(defaultValue = "") String author,
//                           @RequestParam(defaultValue = "") String publisher,
//                           @RequestParam(defaultValue = "") Integer year,
//                           Model model){
//        List<Book> books = bookService.searchBookCondition(new BookSearchCondition(keyword,isbn,author,publisher,year));
//        System.out.println(books.size());
//
//        model.addAttribute("bookList", bookList);
//
//        return "book/searchBookList";
//    }

    // 도서 검색 v3 - 통합검색, 동적쿼리, 페이징처리
    @GetMapping("/main/book/search/result")
    public String searchbookList(@RequestParam(defaultValue = "") String keyword,
                           @RequestParam(defaultValue = "") String isbn,
                           @RequestParam(defaultValue = "") String author,
                           @RequestParam(defaultValue = "") String publisher,
                           @RequestParam(defaultValue = "") Integer year,
                           @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                           Model model){
        Page<Book> bookList = bookService.searchBookConditionPage(new BookSearchCondition(keyword,isbn,author,publisher,year), pageable);

        model.addAttribute("bookList", bookList);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", bookList.hasNext());
        model.addAttribute("hasPrev", bookList.hasPrevious());

        return "book/searchBookList";
    }

    // 네이버 API BOOK 검색 폼
    @GetMapping("/main/book/search/api")
    public String searchBookAPIForm(){
        return "book/api/searchBook";
    }

    // 네이버 API BOOK 검색 결과
    @GetMapping("/main/book/search/api/result")
    public String searchBookAPIList(@RequestParam(defaultValue = "") String keyword,
                                    //Criteria cri,
                                    Model model){

//        PageMaker pageMaker = new PageMaker();
//        pageMaker.setCri(cri);
//        pageMaker.setTotalCount(100);

        BookApiResultDto books = bookApiService.searchBookNaverAPI(keyword);

        model.addAttribute("books", books);

        return "book/api/searchBookResult";
    }

    // 도서 상세 정보
    @GetMapping("/main/book/{id}")
    public String bookDetail(@PathVariable Long id, Model model){
        BookDto book = bookService.findBookById(id);

        List<Book> bookList_temp= bookService.findBookbyISBN(book.getIsbn());

        // ModelMapper이용해 List<Entity> -> List<Dto>
        List<BookListDto> bookList = bookList_temp.stream()
                .map(books->modelMapper.map(books, BookListDto.class))
                .collect(Collectors.toList());

        bookService.setReturnDay(bookList);

        model.addAttribute("book", book);
        model.addAttribute("bookList", bookList);

        return "book/bookDetail";
    }
}
