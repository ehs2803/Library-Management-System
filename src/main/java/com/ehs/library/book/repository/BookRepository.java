package com.ehs.library.book.repository;

import com.ehs.library.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByNameContaining(String keyword);
}
