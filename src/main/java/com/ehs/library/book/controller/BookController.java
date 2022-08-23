package com.ehs.library.book.controller;

import com.ehs.library.book.entity.Book;
import com.ehs.library.book.service.BookService;
import com.ehs.library.bookhope.dto.BookHopeFormDto;
import com.ehs.library.bookhope.service.BookHopeService;
import com.ehs.library.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/main/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/search")
    public String searchBookList(){


        return "book/searchBook";
    }

    @GetMapping("/search/result")
    public String bookList(@RequestParam(defaultValue = "") String keyword,
                             @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model){
        Page<Book> bookList = bookService.searchBookList(keyword, pageable);

        model.addAttribute("bookList", bookList);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", bookList.hasNext());
        model.addAttribute("hasPrev", bookList.hasPrevious());

        return "book/searchBookList";
    }

    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model){
        Book book = bookService.findBookById(id);
        List<Book> bookList= bookService.findBookbyISBN(book.getIsbn());

        model.addAttribute("book", book);
        model.addAttribute("bookList", bookList);

        return "book/bookDetail";
    }
}
