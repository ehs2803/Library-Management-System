package com.ehs.library.book.repository;

import com.ehs.library.book.dto.BookDto;
import com.ehs.library.book.dto.BookSearchCondition;
import com.ehs.library.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> searchBook(BookSearchCondition condition);

    Page<Book> searchBookPageSimple(BookSearchCondition condition, Pageable pageable);
}
