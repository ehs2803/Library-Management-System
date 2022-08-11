package com.ehs.library.book.repository;

import com.ehs.library.book.entity.Book;
import com.ehs.library.member.constant.Role;
import com.ehs.library.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByNameContaining(String keyword);

    @EntityGraph(attributePaths = {"bookImg"})
    Page<Book> findByNameContaining(String keyword, Pageable pageable);
}
